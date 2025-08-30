/**
 * Copyright 2025 Shotadft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shotadft.kanaconverter.io

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.shotadft.kanaconverter.KanaConverter.GROUP
import com.shotadft.kanaconverter.KanaConverter.NAME
import com.shotadft.kanaconverter.KanaConverter.VERSION
import com.shotadft.kanaconverter.io.util.CompressUtil.gzip
import com.shotadft.kanaconverter.io.util.CompressUtil.ungzip
import com.shotadft.kanaconverter.map.util.LinkedFastStrMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Instant
import java.util.zip.CRC32

/**
 * @author Shotadft
 * @since 1.1
 */
internal class CacheManager {

    /**
     * Saves the given map to a cache file. The data is serialized to JSON,
     * compressed with Gzip, and stored along with a CRC32 checksum for integrity.
     *
     * @param fileName The name of the cache file.
     * @param data The LinkedFastStrMap to be saved.
     * @param ttlSeconds Time-to-live in seconds.
     * @author Shotadft
     * @since 1.1
     */
    fun save(fileName: String, data: LinkedFastStrMap, ttlSeconds: Long) {
        val path = getCachePath(fileName)
        path.parentFile?.mkdirs()

        val serialized = serializeMap(data)
        val compressedData = gzip(serialized)

        // Create Header (16 bytes) with pre-allocated buffer
        val expireAt = Instant.now().epochSecond + ttlSeconds
        val header = ByteBuffer.allocate(HEADER_SIZE)
            .order(ByteOrder.BIG_ENDIAN)
            .putInt(HEADER_SIZE)
            .putInt(CACHE_VERSION)
            .putLong(expireAt)
            .array()

        val body = header + compressedData

        // Calculate CRC32 and write file
        val crcValue = calculateCRC32(body)
        val finalData = ByteBuffer.allocate(body.size + CRC_SIZE)
            .put(body)
            .putInt(crcValue)
            .array()

        path.writeBytes(finalData)
    }

    /**
     * Loads a cached map from a file and verifies its integrity.
     * The file is decompressed and its CRC32 checksum is compared with the stored checksum.
     *
     * @param fileName The name of the cache file to load.
     * @return The decompressed and deserialized LinkedFastStrMap.
     * @throws IllegalStateException if the cache is corrupted, expired, or has wrong version.
     * @throws FileNotFoundException if the cache file does not exist.
     * @author Shotadft
     * @since 1.1
     */
    fun load(fileName: String): LinkedFastStrMap {
        val path = getCachePath(fileName)
        if (!path.exists()) {
            throw FileNotFoundException("Cache file not found: ${path.absolutePath}")
        }

        val allBytes = path.readBytes()
        if (allBytes.size < MIN_FILE_SIZE) {
            throw IllegalStateException("Cache file corrupted: file too small")
        }

        // Extract and verify CRC
        val body = allBytes.sliceArray(0 until allBytes.size - CRC_SIZE)
        val storedCrc = ByteBuffer.wrap(allBytes, allBytes.size - CRC_SIZE, CRC_SIZE)
            .order(ByteOrder.BIG_ENDIAN)
            .int

        val calculatedCrc = calculateCRC32(body)
        if (calculatedCrc != storedCrc) {
            throw IllegalStateException("CRC check failed! Data may be corrupted.")
        }

        // Parse and validate header
        val headerBuffer = ByteBuffer.wrap(body, 0, HEADER_SIZE).order(ByteOrder.BIG_ENDIAN)
        val headerLength = headerBuffer.int
        if (headerLength != HEADER_SIZE) {
            throw IllegalStateException("Invalid header length: $headerLength")
        }

        val version = headerBuffer.int
        if (version != CACHE_VERSION) {
            throw IllegalStateException("Unsupported cache version: $version")
        }

        val expireAt = headerBuffer.long
        if (Instant.now().epochSecond > expireAt) {
            throw IllegalStateException("Cache expired!")
        }

        // Decompress and deserialize
        val compressedData = body.sliceArray(HEADER_SIZE until body.size)
        val decompressedData = ungzip(compressedData)
        return deserializeMap(decompressedData)
    }

    private fun calculateCRC32(data: ByteArray): Int {
        val crc = CRC32()
        crc.update(data)
        return (crc.value and 0xffffffffL).toInt()
    }

    private companion object {
        private val pathCache = hashMapOf<String, File>()

        private val mapper by lazy {
            ObjectMapper()
                .registerKotlinModule()
                .findAndRegisterModules()
        }

        private const val HEADER_SIZE = 16
        private const val CRC_SIZE = 4
        private const val MIN_FILE_SIZE = HEADER_SIZE + CRC_SIZE
        private const val CACHE_VERSION = (VERSION * 10) + MIN_FILE_SIZE

        /**
         * Serializes a LinkedFastStrMap to a byte array efficiently.
         * Uses direct byte array serialization to avoid string encoding overhead.
         */
        private fun serializeMap(map: LinkedFastStrMap): ByteArray {
            val serializable = map.mapValues { it.value.toList() }
            return mapper.writeValueAsBytes(serializable)
        }

        /**
         * Deserializes a byte array containing JSON data into a LinkedFastStrMap.
         * Optimized for direct conversion without intermediate objects.
         */
        private fun deserializeMap(json: ByteArray): LinkedFastStrMap {
            val typeRef = object : TypeReference<Map<String, List<String>>>() {}
            val deserializedMap: Map<String, List<String>> = mapper.readValue(json, typeRef)

            // Efficient conversion back to LinkedFastStrMap
            val result = LinkedFastStrMap()
            deserializedMap.forEach { (key, valueList) ->
                result[key] = ObjectOpenHashSet<String>().apply { addAll(valueList) }
            }
            return result
        }

        /**
         * Determines the appropriate cache path based on the operating system.
         * Uses cache to avoid repeated system property lookups.
         */
        private fun getCachePath(fileName: String): File {
            return pathCache.computeIfAbsent(fileName) { name ->
                val os = System.getProperty("os.name")?.lowercase() ?: ""
                val userHome = System.getProperty("user.home") ?: ""
                val cachePath = "$GROUP.$NAME"

                when {
                    os.contains("win") ->
                        File("$userHome\\AppData\\Local\\Temp\\$cachePath\\$name")

                    os.contains("mac") ->
                        File("$userHome/Library/Application Support/$cachePath/$name")

                    else ->
                        File("$userHome/.cache/$cachePath/$name")
                }
            }
        }
    }
}