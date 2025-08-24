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

import com.shotadft.kanaconverter.api.ConvertType
import com.shotadft.kanaconverter.api.ConvertType.JAPANESE
import com.shotadft.kanaconverter.api.ConvertType.ROMAJI
import com.shotadft.kanaconverter.util.ConvertUtil

/**
 * @author Shotadft
 * @since 1.1
 */
class KanaConverter {
    companion object {
        /**
         * @author Shotadft
         * @since 1.0
         */
        @JvmStatic
        fun String.toHiragana(type: ConvertType = ROMAJI) = when (type) {
            ROMAJI -> this // TODO: ローマ字からの変換処理
            JAPANESE -> ConvertUtil.toHiragana(this)
        }

        /**
         * @author Shotadft
         * @since 1.0
         */
        @JvmStatic
        fun String.toKatakana(type: ConvertType = ROMAJI) = when (type) {
            ROMAJI -> this // TODO: ローマ字からの変換処理
            JAPANESE -> ConvertUtil.toKatakana(this)
        }

        /**
         * @author Shotadft
         * @since 1.1
         */
        @JvmStatic
        fun String.toRomaji() = this // TODO: ひらがな/カタカナからローマ字へ変換
    }
}
