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
package com.shotadft.kanaconverter.test

import com.shotadft.kanaconverter.KanaConverter.toHiragana
import com.shotadft.kanaconverter.KanaConverter.toKatakana
import com.shotadft.kanaconverter.map.Mapper
import kotlin.test.Test
import kotlin.test.assertEquals

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
internal class Tester {
    @Test
    fun `Convert Hiragana Test`() {
        val str = "aiueo ka kiku ke koo hattya! aidobe-! nihahaha! uiaeo syasharideru amuro rei kunnnn nnunn"
        println("Original -> \"$str\"")
        val res = str.toHiragana()
        println("toHiragana -> \"$res\"")
        val expected = "あいうえお か きく け こお はっちゃ! あいどべ-! にははは! ういあえお しゃしゃりでる あむろ れい くんん んうん"
        println("想定     　 -> \"$expected\"")
        assertEquals(expected, res)
        println()
    }

    @Test
    fun `Convert Katakana Test`() {
        val str = "aiueo ka kiku ke koo hattya! aidobe-! nihahaha! uiaeo syasharideru amuro rei kunnnn nnunn"
        println("Original -> \"$str\"")
        val res = str.toKatakana()
        println("toKatakana -> \"$res\"")
        val expected = "アイウエオ カ キク ケ コオ ハッチャ! アイドベ-! ニハハハ! ウイアエオ シャシャリデル アムロ レイ クンン ンウン"
        println("想定     　 -> \"$expected\"")
        assertEquals(expected, res)
        println()
    }

    @Test
    fun `Check Map`() {
        val map = Mapper.h2rMap
        val rev = Mapper.r2hMap
        println("Res -> \n$map\nRev -> \n$rev\n")
    }
}