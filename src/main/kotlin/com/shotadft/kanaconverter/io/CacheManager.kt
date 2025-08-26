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
import com.shotadft.kanaconverter.io.util.CompressUtil.gzip
import com.shotadft.kanaconverter.io.util.CompressUtil.ungzip
import com.shotadft.kanaconverter.map.util.LinkedFastStrMap
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.time.Duration
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
     * @author Shotadft
     * @since 1.1
     */
    fun save(fileName: String, data: LinkedFastStrMap, ttlSeconds: Long = Duration.ofDays(1).seconds) {
        val path = getCachePath(fileName)
        path.parentFile?.mkdirs()

        val serialized = serializeMap(data).encodeToByteArray()
        val compressedData = gzip(serialized)

        val expireAt = Instant.now().epochSecond + ttlSeconds
        val headerBuffer = ByteBuffer.allocate(4 + 4 + 8)
        headerBuffer.putInt(16)
        headerBuffer.putInt(CACHE_VERSION)
        headerBuffer.putLong(expireAt)
        val headerBytes = headerBuffer.array()

        val finalBytes = headerBytes + compressedData

        path.writeBytes(finalBytes)

        val crc = CRC32()
        crc.update(finalBytes)
        val checksum = crc.value
        val crcBytes = ByteBuffer.allocate(8).putLong(checksum).array()
        File("${path.absolutePath}.crc").writeBytes(crcBytes)
    }

    /**
     * Loads a cached map from a file and verifies its integrity.
     * The file is decompressed and its CRC32 checksum is compared with the stored checksum.
     *
     * @param fileName The name of the cache file to load.
     * @return The decompressed and deserialized LinkedFastStrMap.
     * @throws IllegalStateException if the cache files are not found or if the CRC check fails.
     * @author Shotadft
     * @since 1.1
     */
    fun load(fileName: String): LinkedFastStrMap {
        val path = getCachePath(fileName)
        if (!exists(fileName))
            throw FileNotFoundException("Cache files not found: ${path.absolutePath}")

        val allBytes = path.readBytes()

        val crc = CRC32()
        crc.update(allBytes)
        val calculatedCrc = crc.value
        val storedCrc = ByteBuffer.wrap(File("${path.absolutePath}.crc").readBytes()).long
        if (calculatedCrc != storedCrc)
            throw IllegalStateException("CRC check failed! Data may be corrupted.")

        val headerLength = ByteBuffer.wrap(allBytes.sliceArray(0..3)).int
        if (headerLength != 16)
            throw IllegalStateException("Invalid header length")

        val headerBuffer = ByteBuffer.wrap(allBytes.sliceArray(0 until 16))
        headerBuffer.position(4)
        val version = headerBuffer.int
        val expireAt = headerBuffer.long

        if (Instant.now().epochSecond > expireAt)
            throw IllegalStateException("Cache expired! Version: $version")

        val compressedData = allBytes.sliceArray(16 until allBytes.size)
        val decompressedData = ungzip(compressedData)
        return deserializeMap(decompressedData)
    }

    private companion object {
        private val mapper by lazy { ObjectMapper().registerKotlinModule() }

        private const val CACHE_VERSION = 11_08923812

        /**
         * Serializes a LinkedFastStrMap to a JSON string.
         *
         * @param map The map to serialize.
         * @return A JSON string representing the map.
         * @author Shotadft
         * @since 1.1
         */
        private fun serializeMap(map: LinkedFastStrMap): String =
            mapper.writeValueAsString(map.mapValues { it.value.toList() })

        /**
         * Deserializes a byte array containing JSON data into a LinkedFastStrMap.
         *
         * @param json The byte array of JSON data.
         * @return The deserialized LinkedFastStrMap.
         * @author Shotadft
         * @since 1.1
         */
        private fun deserializeMap(json: ByteArray): LinkedFastStrMap =
            mapper.readValue(json, object : TypeReference<LinkedFastStrMap>() {
                override fun getType() = mapper.typeFactory.constructMapType(
                    Object2ObjectLinkedOpenHashMap::class.java,
                    String::class.java,
                    ObjectOpenHashSet::class.java
                )
            })

        /**
         * Checks if the cache files (data and CRC) for the given file name exist.
         *
         * @param fileName The name of the cache file.
         * @return `true` if both the data and CRC files exist, `false` otherwise.
         * @author Shotadft
         * @since 1.1
         */
        private fun exists(fileName: String): Boolean {
            val path = getCachePath(fileName)
            return path.exists() && File("${path.absolutePath}.crc").exists()
        }

        /**
         * Determines the appropriate cache path based on the operating system.
         *
         * @param fileName The name of the file to be cached.
         * @return The platform-specific File path.
         * @author Shotadft
         * @since 1.1
         */
        private fun getCachePath(fileName: String): File {
            val os = System.getProperty("os.name").lowercase()
            val userHome = System.getProperty("user.home")

            return when {
                os.contains("win") -> {
                    File("$userHome\\AppData\\Local\\Temp\\$fileName")
                }

                os.contains("mac") -> {
                    File("$userHome/Library/Application Support/com.shotadft.kanaconverter/$fileName")
                }

                else -> {
                    File("$userHome/.cache/com.shotadft.kanaconverter/$fileName")
                }
            }
        }
    }
}