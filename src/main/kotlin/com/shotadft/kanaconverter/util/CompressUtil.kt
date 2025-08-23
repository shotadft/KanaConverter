package com.shotadft.kanaconverter.util

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * @author Shotadft
 * @since 1.1
 */
internal object CompressUtil {
    /**
     * Compresses the given [ByteArray] using the Gzip compression algorithm.
     * @param data The byte array to compress.
     * @return A new byte array containing the compressed data.
     */
    fun gzip(data: ByteArray): ByteArray {
        ByteArrayOutputStream().use { bos ->
            GzipCompressorOutputStream(bos).use { gzip ->
                gzip.write(data)
            }
            return bos.toByteArray()
        }
    }

    /**
     * Decompresses the given Gzip-compressed [ByteArray].
     *
     * @param data The Gzip-compressed byte array to decompress.
     * @return A new byte array containing the decompressed data.
     */
    fun ungzip(data: ByteArray): ByteArray {
        ByteArrayInputStream(data).use { bis ->
            GzipCompressorInputStream(bis).use { gzip ->
                return gzip.readBytes()
            }
        }
    }
}