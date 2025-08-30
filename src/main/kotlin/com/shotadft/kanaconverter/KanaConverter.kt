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
package com.shotadft.kanaconverter

import com.shotadft.kanaconverter.converter.KanaConverter
import com.shotadft.kanaconverter.converter.RomajiConverter
import com.shotadft.kanaconverter.converter.type.ConvertType
import com.shotadft.kanaconverter.converter.type.ConvertType.JAPANESE
import com.shotadft.kanaconverter.converter.type.ConvertType.ROMAJI
import com.shotadft.kanaconverter.util.ConvertUtil

/**
 * @author Shotadft
 * @since 1.1
 */
@Suppress("Unused")
object KanaConverter {
    internal const val GROUP = "com.shotadft"
    internal const val NAME = "kanaconverter"
    internal const val VERSION = 1_1_2

    /**
     * Converts this [CharSequence] to Hiragana.
     *
     * The conversion method depends on [type]:
     * - [ROMAJI]: Converts from Romaji to Hiragana.
     * - [JAPANESE]: Converts from Japanese text to Hiragana.
     *
     * @param type The conversion type (default is [ROMAJI]).
     * @return A string containing the converted Hiragana text.
     * @author Shotadft
     * @since 1.0
     */
    @JvmStatic
    fun CharSequence.toHiragana(type: ConvertType = ROMAJI): String = when (type) {
        ROMAJI -> RomajiConverter().convert(this)
        JAPANESE -> ConvertUtil.toHiragana(this)
    }

    /**
     * Converts this [CharSequence] to Katakana.
     *
     * The conversion method depends on [type]:
     * - [ROMAJI]: Converts from Romaji to Hiragana first, then to Katakana.
     * - [JAPANESE]: Converts from Japanese text directly to Katakana.
     *
     * @param type The conversion type (default is [ROMAJI]).
     * @return A string containing the converted Katakana text.
     * @author Shotadft
     * @since 1.0
     */
    @JvmStatic
    fun CharSequence.toKatakana(type: ConvertType = ROMAJI): String = when (type) {
        ROMAJI -> ConvertUtil.toKatakana(this.toHiragana(type = ROMAJI))
        JAPANESE -> ConvertUtil.toKatakana(this)
    }

    /**
     * Converts this [CharSequence] from Japanese text to Romaji.
     *
     * First converts the text to Hiragana, then converts the Hiragana to Romaji.
     *
     * @return A string containing the converted Romaji text.
     * @author Shotadft
     * @since 1.1
     */
    @JvmStatic
    fun CharSequence.toRomaji(): String {
        val unified = ConvertUtil.toHiragana(this)
        return KanaConverter().convert(unified)
    }
}
