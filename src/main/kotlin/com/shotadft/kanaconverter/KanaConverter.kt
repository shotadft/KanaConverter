package com.shotadft.kanaconverter

import com.shotadft.kanaconverter.api.ConvertType
import com.shotadft.kanaconverter.api.ConvertType.*
import kotlin.text.map

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
        ROMAJI -> this
        JAPANESE ->
            this.map { if (it in '\u30A1'..'\u30F6') it - 0x60 else it }.joinToString("")
    }

    /**
     * @author Shotadft
     * @since 1.0
     */
    @JvmStatic
    fun String.toKatakana(type: ConvertType = ROMAJI) = when (type) {
        ROMAJI -> this
        JAPANESE ->
            this.map { if (it in '\u3041'..'\u3096') it + 0x60 else it }.joinToString("")
    }
}
