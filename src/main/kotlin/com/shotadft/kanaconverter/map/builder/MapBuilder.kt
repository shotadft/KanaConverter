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
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.CONSONANTS
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.KANA
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.N_CONSONANTS
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.VOWELS
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.cacheManager
import com.shotadft.kanaconverter.map.util.LinkedFastStrMap
import com.shotadft.kanaconverter.map.util.MapperUtil.buildLinkedFastMap
import com.shotadft.kanaconverter.map.util.MapperUtil.objectLinkedOpenSetOf
import com.shotadft.kanaconverter.map.util.MapperUtil.objectOpenSetOf

/**
 * @author Shotadft
 * @since 1.1
 */
internal class MapBuilder {
    /**
     * Constructs a [LinkedFastStrMap] with optional caching for faster subsequent retrievals.
     *
     * The build process works as follows:
     * 1. Attempts to load a previously cached map from [CACHE_NAME] via [cacheManager].
     * 2. If loading fails or no cache exists, generates a new map using [buildBaseMap].
     * 3. The newly built map is then saved to the cache for future use.
     *
     * The resulting map associates each Japanese kana character with a set of romanized
     * representations (consonant + vowel). Special handling includes:
     * - The character "ん" is mapped to [N_CONSONANTS].
     *
     * Usage ensures that repeated calls to [build] are optimized via caching, avoiding
     * the need to reconstruct the base map each time.
     *
     * @return A [LinkedFastStrMap] where keys are kana characters and values are sets of romanized strings.
     * @throws Exception If the cache operation fails in a way that is not caught internally.
     * @author Shotadft
     * @since 1.1
     */
    internal fun build(): LinkedFastStrMap = runCatching { cacheManager.load(CACHE_NAME) }
        .onFailure { it.printStackTrace() }
        .getOrNull() ?: buildBaseMap()
        .apply {

        }.also { cacheManager.save(CACHE_NAME, it) }

    /**
     * Builds the base kana-to-romanization map without using the cache.
     *
     * Each entry maps a kana character to a set containing the corresponding
     * consonant+vowel combination. The mapping is generated using the predefined
     * [KANA], [CONSONANTS], and [VOWELS] collections.
     *
     * Special cases:
     * - The kana "ん" is mapped to [N_CONSONANTS].
     *
     * @return A [LinkedFastStrMap] containing the complete kana-to-romanization mapping.
     * @author Shotadft
     * @since 1.1
     */
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

    private companion object {
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