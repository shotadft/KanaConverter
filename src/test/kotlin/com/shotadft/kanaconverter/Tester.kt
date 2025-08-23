package com.shotadft.kanaconverter

import com.shotadft.kanaconverter.KanaConverter.toHiragana
import com.shotadft.kanaconverter.KanaConverter.toKatakana
import com.shotadft.kanaconverter.api.ConvertType
import kotlin.test.Test

internal class Tester {
    @Test
    fun `Convert Test`() {
        println("ひらがな".toKatakana(ConvertType.JAPANESE))
        println("カタカナ".toHiragana(ConvertType.JAPANESE))
    }
}