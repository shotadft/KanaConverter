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
import com.shotadft.kanaconverter.map.util.StrSet

internal class KanaConverter : IConverter {
    /**
     * @author Shotadft
     * @since 1.1
     */
    override fun convert(input: CharSequence): String = buildString {
        var i = 0
        while (i < input.length) {
            var matched = false

            if (input[i] == 'っ' && i + 1 < input.length) {
                val next = input[i + 1].toString()
                val nextRomans = h2rMap[next]
                if (!nextRomans.isNullOrEmpty()) {
                    append(nextRomans.first()[0])
                    i++
                    continue
                }
            }

            for (len in 2 downTo 1) {
                if (i + len <= input.length) {
                    val sub = input.substring(i, i + len)
                    val romans = h2rMap[sub]
                    if (!romans.isNullOrEmpty()) {
                        append(selectCandidate(romans))
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

    /**
     * Helper function to select a single romanization from candidates.
     */
    private fun selectCandidate(candidates: StrSet): String = candidates.first() ?: ""

    private companion object {
        private val h2rMap = Mapper.h2rMap
    }
}