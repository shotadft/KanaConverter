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
package com.shotadft.kanaconverter.converter

import com.shotadft.kanaconverter.map.Mapper

/**
 * @author Shotadft
 * @since 1.1
 */
internal class RomajiConverter : IConverter {
    /**
     * Convert Roman letters to Hiragana
     * @author Shotadft
     * @since 1.1
     */
    override fun convert(input: CharSequence): String = buildString {
        var i = 0
        while (i < input.length) {
            val c = input[i].lowercaseChar()
            var matched = false

            when {
                i + 1 < input.length &&
                        c in 'a'..'z' && c !in listOf('a', 'i', 'u', 'e', 'o', 'n') &&
                        input[i + 1].lowercaseChar() == c -> {
                    var count = 0
                    while (i + count < input.length &&
                        input[i + count].lowercaseChar() == c
                    ) count++
                    repeat(count - 1) { append('っ') }
                    i += count - 1
                    matched = true
                }

                else -> {
                    for (len in 3 downTo 1) {
                        if (i + len <= input.length) {
                            val sub = input.subSequence(i, i + len).toString().lowercase()
                            val kana = r2hMap[sub]
                            if (kana != null) {
                                append(kana)
                                i += len
                                matched = true
                                break
                            }
                        }
                    }
                }
            }

            if (!matched) {
                append(input[i])
                i++
            }
        }
    }

    private companion object {
        private val r2hMap = Mapper.r2hMap
    }
}