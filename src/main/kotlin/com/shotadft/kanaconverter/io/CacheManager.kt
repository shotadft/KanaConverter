package com.shotadft.kanaconverter.io

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.shotadft.kanaconverter.util.CompressUtil.gzip
import com.shotadft.kanaconverter.util.CompressUtil.ungzip
import com.shotadft.kanaconverter.util.LinkedFastStrMap
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import java.io.File
import java.util.zip.CRC32

internal class CacheManager {

    fun save(fileName: String, data: LinkedFastStrMap) {
        val path = getCachePath(fileName)
        path.parentFile?.mkdirs()

        val serialized = serializeMap(data)
        val byteData = serialized.encodeToByteArray()

        val crc = CRC32()
        crc.update(byteData)
        val checksum = crc.value

        File("${path.absolutePath}.crc").writeBytes(checksum.toInt().toByteArray())
        path.writeBytes(gzip(byteData))
    }

    fun load(fileName: String): LinkedFastStrMap {
        val path = getCachePath(fileName)
        if (!exists(fileName)) throw IllegalStateException("Cache files not found: ${path.absolutePath}")

        val decompressedData = ungzip(path.readBytes())

        val crc = CRC32()
        crc.update(decompressedData)
        val calculatedCrc = crc.value

        val storedCrc = File("${path.absolutePath}.crc").readBytes().toLong()

        if (calculatedCrc != storedCrc) {
            throw IllegalStateException("CRC check failed! Data may be corrupted.")
        }

        return deserializeMap(decompressedData)
    }

    private companion object {
        val mapper by lazy { ObjectMapper().registerKotlinModule() }

        fun serializeMap(map: LinkedFastStrMap): String =
            mapper.writeValueAsString(map.mapValues { it.value.toList() })

        fun deserializeMap(json: ByteArray): LinkedFastStrMap {
            return mapper.readValue(json, object : TypeReference<LinkedFastStrMap>() {
                override fun getType() = mapper.typeFactory.constructMapType(
                    Object2ObjectLinkedOpenHashMap::class.java,
                    String::class.java,
                    ObjectOpenHashSet::class.java
                )
            })
        }

        fun exists(fileName: String): Boolean {
            val path = getCachePath(fileName)
            return path.exists() && File("${path.absolutePath}.crc").exists()
        }

        fun getCachePath(fileName: String): File {
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

        fun Int.toByteArray(): ByteArray {
            return byteArrayOf(
                (this shr 24).toByte(),
                (this shr 16).toByte(),
                (this shr 8).toByte(),
                this.toByte()
            )
        }

        fun ByteArray.toLong(): Long {
            return ((this[0].toInt() and 0xFF) shl 24 or
                    (this[1].toInt() and 0xFF) shl 16 or
                    (this[2].toInt() and 0xFF) shl 8 or
                    (this[3].toInt() and 0xFF)).toLong()
        }
    }
}