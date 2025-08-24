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
package com.shotadft.kanaconverter.util

/**
 * @author Shotadft
 * @since 1.1
 */
internal object ConvertUtil {
    private fun isHiragana(c: Char): Boolean = c in '\u3041'..'\u3096'
    private fun isKatakana(c: Char): Boolean = c in '\u30A1'..'\u30F6'

    /**
     * @author Shotadft
     * @since 1.1
     */
    @JvmStatic
    fun toHiragana(s: CharSequence) =
        s.map { if (isKatakana(it)) it - 0x60 else it }.joinToString("")

    /**
     * @author Shotadft
     * @since 1.1
     */
    @JvmStatic
    fun toKatakana(s: CharSequence) =
        s.map { if (isHiragana(it)) it + 0x60 else it }.joinToString("")
}