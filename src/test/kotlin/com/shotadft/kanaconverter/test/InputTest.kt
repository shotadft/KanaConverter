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

import com.shotadft.kanaconverter.KanaConverter.Companion.toHiragana
import com.shotadft.kanaconverter.KanaConverter.Companion.toKatakana

fun main() {
    val str = readlnOrNull()
    if (str != null) {
        val res1 = str.toHiragana()
        val res2 = str.toKatakana()
        println("toHiragana -> \"$res1\"")
        println("toKatakana -> \"$res2\"")
    }
}