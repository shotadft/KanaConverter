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

import com.shotadft.kanaconverter.converter.RomajiConverter
import com.shotadft.kanaconverter.type.ConvertType
import com.shotadft.kanaconverter.type.ConvertType.JAPANESE
import com.shotadft.kanaconverter.type.ConvertType.ROMAJI
import com.shotadft.kanaconverter.util.ConvertUtil

/**
 * @author Shotadft
 * @since 1.1
 */
@Suppress("Unused")
class KanaConverter {
    companion object {
        /**
         * @author Shotadft
         * @since 1.0
         */
        @JvmStatic
        fun CharSequence.toHiragana(type: ConvertType = ROMAJI): String = when (type) {
            ROMAJI -> RomajiConverter().convert(this)
            JAPANESE -> ConvertUtil.toHiragana(this)
        }

        /**
         * @author Shotadft
         * @since 1.0
         */
        @JvmStatic
        fun CharSequence.toKatakana(type: ConvertType = ROMAJI): String = when (type) {
            ROMAJI -> ConvertUtil.toKatakana(this.toHiragana(type = ROMAJI))
            JAPANESE -> ConvertUtil.toKatakana(this)
        }

        /**
         * @author Shotadft
         * @since 1.1
         */
        @JvmStatic
        fun CharSequence.toRomaji(): String = this.toString() // TODO: ひらがな/カタカナからローマ字へ変換
    }
}
