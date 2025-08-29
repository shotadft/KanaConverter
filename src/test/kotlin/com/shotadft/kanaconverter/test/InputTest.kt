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
import com.shotadft.kanaconverter.KanaConverter.toRomaji

fun main() {
    val (h, k) = a()
    val r = b()
    println("toHiragana -> \"$h\"")
    println("toKatakana -> \"$k\"")
    println("toRomaji   -> \"$r\"")
}

private fun a(): Pair<String, String> {
    print("a: ")
    val str1 = readln()
    return str1.toHiragana() to str1.toKatakana()
}

private fun b(): String {
    print("b: ")
    val str2 = readln()
    return str2.toRomaji()
}