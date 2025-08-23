package com.shotadft.kanaconverter.util

/**
 * @author Shotadft
 * @since 1.1
 */
internal object ConvertUtil {
    /**
     * @author Shotadft
     * @since 1.1
     */
    @JvmStatic
    fun toHiragana(s: CharSequence) =
        s.map { if (it in '\u30A1'..'\u30F6') it - 0x60 else it }.joinToString("")

    /**
     * @author Shotadft
     * @since 1.1
     */
    @JvmStatic
    fun toKatakana(s: CharSequence) =
        s.map { if (it in '\u3041'..'\u3096') it + 0x60 else it }.joinToString("")
}