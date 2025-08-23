package com.shotadft.kanaconverter.util

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal object CompressUtil {
    fun gzip(data: ByteArray): ByteArray {
        ByteArrayOutputStream().use { bos ->
            GzipCompressorOutputStream(bos).use { gzip ->
                gzip.write(data)
            }
            return bos.toByteArray()
        }
    }

    fun ungzip(data: ByteArray): ByteArray {
        ByteArrayInputStream(data).use { bis ->
            GzipCompressorInputStream(bis).use { gzip ->
                return gzip.readBytes()
            }
        }
    }
}