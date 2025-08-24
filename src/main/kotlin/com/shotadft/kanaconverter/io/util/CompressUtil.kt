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
package com.shotadft.kanaconverter.io.util

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
    internal fun gzip(data: ByteArray): ByteArray {
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
    internal fun ungzip(data: ByteArray): ByteArray {
        ByteArrayInputStream(data).use { bis ->
            GzipCompressorInputStream(bis).use { gzip ->
                return gzip.readBytes()
            }
        }
    }
}