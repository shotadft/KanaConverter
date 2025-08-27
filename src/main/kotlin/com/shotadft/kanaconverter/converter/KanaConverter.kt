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
internal class KanaConverter : IConverter {
    /**
     * Convert Roman letters to hiragana
     * @author Shotadft
     * @since 1.1
     */
    override fun convert(input: CharSequence): String = buildString {
        var i = 0
        while (i < input.length) {
            var matched = false

            for (len in 3 downTo 1) {
                if (i + len <= input.length) {
                    val sub = input.subSequence(i, i + len).toString().lowercase()
                    val kana = h2rMap[sub]
                    if (kana != null) {
                        append(kana.first())
                        i += len
                        matched = true
                        break
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
        private val h2rMap = Mapper.h2rMap
    }
}