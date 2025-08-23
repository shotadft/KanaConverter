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