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
package com.shotadft.kanaconverter.map.builder

import com.shotadft.kanaconverter.io.CacheManager
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.CACHE_NAME
import com.shotadft.kanaconverter.util.LinkedFastStrMap
import com.shotadft.kanaconverter.util.MapperUtil.buildLinkedFastMap
import com.shotadft.kanaconverter.util.MapperUtil.objectLinkedOpenSetOf
import com.shotadft.kanaconverter.util.MapperUtil.objectOpenSetOf

/**
 * @author Shotadft
 * @since 1.1
 */
internal class MapBuilder {
    /**
     * Builds a new [LinkedFastStrMap], optionally loading from a cache for improved performance.
     *
     * This function first attempts to load an existing map from the cache named [CACHE_NAME].
     * If the cache load is successful, the cached map is used directly. Otherwise, a new
     * map is built from a base map and then saved to the cache for future use.
     *
     * @return A new [LinkedFastStrMap] instance.
     * @author Shotadft
     * @since 1.1
     */
    fun build(): LinkedFastStrMap = buildLinkedFastMap {
        val map = runCatching { cacheManager.load(CACHE_NAME) }
            .onFailure { it.printStackTrace() }
            .getOrNull() ?: buildBaseMap().apply {

        }.also { cacheManager.save(CACHE_NAME, it) }

        putAll(map)
    }

    private fun buildBaseMap(): LinkedFastStrMap = buildLinkedFastMap {
        KANA.forEachIndexed { i, row ->
            val consonant = CONSONANTS.elementAtOrNull(i)?.toString() ?: ""

            row.forEachIndexed { j, column ->
                if (column != null) {
                    val vowel = VOWELS.elementAtOrNull(j) ?: return@forEachIndexed
                    put(column.toString(), objectOpenSetOf("$consonant$vowel"))
                }
            }
        }

        put("ん", N_CONSONANTS)
    }

    companion object {
        private const val CACHE_NAME = "MAPCACHE.bin"
        private val cacheManager = CacheManager()

        private val VOWELS = objectLinkedOpenSetOf('a', 'i', 'u', 'e', 'o')
        private val CONSONANTS = objectLinkedOpenSetOf(null, 'k', 's', 't', 'n', 'h', 'm', 'y', 'r', 'w')
        private val N_CONSONANTS = objectOpenSetOf("n", "n'", "nn")
        private val KANA = listOf<List<Char?>>(
            listOf('あ', 'い', 'う', 'え', 'お'),
            listOf('か', 'き', 'く', 'け', 'こ'),
            listOf('さ', 'し', 'す', 'せ', 'そ'),
            listOf('た', 'ち', 'つ', 'て', 'と'),
            listOf('な', 'に', 'ぬ', 'ね', 'の'),
            listOf('は', 'ひ', 'ふ', 'へ', 'ほ'),
            listOf('ま', 'み', 'む', 'め', 'も'),
            listOf('や', null, 'ゆ', null, 'よ'),
            listOf('ら', 'り', 'る', 'れ', 'ろ'),
            listOf('わ', null, null, null, 'を')
        )
    }
}