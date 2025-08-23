package com.shotadft.kanaconverter

import com.shotadft.kanaconverter.api.ConvertType
import com.shotadft.kanaconverter.api.ConvertType.JAPANESE
import com.shotadft.kanaconverter.api.ConvertType.ROMAJI
import com.shotadft.kanaconverter.util.ConvertUtil

/**
 * @author Shotadft
 * @since 1.1
 */
object KanaConverter {
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
