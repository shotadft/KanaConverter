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
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.CONSONANTS
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.KANA
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.N_CONSONANTS
import com.shotadft.kanaconverter.map.builder.MapBuilder.Companion.VOWELS
import com.shotadft.kanaconverter.map.util.LinkedFastStrMap
import com.shotadft.kanaconverter.map.util.MapperUtil.buildLinkedFastMap
import com.shotadft.kanaconverter.map.util.MapperUtil.linkedFastMapOf
import com.shotadft.kanaconverter.map.util.MapperUtil.objectLinkedOpenSetOf
import com.shotadft.kanaconverter.map.util.MapperUtil.objectOpenSetOf
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
import java.io.FileNotFoundException
import java.time.Duration

/**
 * @author Shotadft
 * @since 1.1
 */
internal class MapBuilder {
    /**
     * Constructs a [LinkedFastStrMap] with optional caching for faster subsequent retrievals.
     *
     * The build process works as follows:
     * 1. Attempts to load a previously cached map.
     * 2. If loading fails or no cache exists, generates a new map using [buildBaseMap].
     * 3. The newly built map is then saved to the cache for future use.
     *
     * The resulting map associates each Japanese kana character with a set of romanized
     * representations (consonant + vowel).
     *
     * Usage ensures that repeated calls to [build] are optimized via caching, avoiding
     * the need to reconstruct the base map each time.
     *
     * @return A [LinkedFastStrMap] where keys are kana characters and values are sets of romanized strings.
     * @throws Exception If the cache operation fails in a way that is not caught internally.
     * @author Shotadft
     * @since 1.1
     */
    internal fun build(): LinkedFastStrMap =
        runCatching { cacheManager.load(CACHE_NAME) }
            .onFailure {
                if (it is FileNotFoundException) System.err.println(it.message)
                else it.printStackTrace()
            }
            .getOrNull() ?: buildBaseMap().apply {
            listOf(
                buildVCMap(),
                buildSmallKanaMap(),
                buildSulkyMap()
            ).forEach { putAll(it) }
            addHepburnMap(this)
            putAll(buildSidetoneMap(this))
        }.also {
            cacheManager.save(
                fileName = CACHE_NAME,
                data = it,
                ttlSeconds = Duration.ofHours(2L).toSeconds()
            )
        }

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
        putAll(buildEachIMap(CONSONANTS, KANA))
        put("ん", N_CONSONANTS)
    }

    private fun buildVCMap(): LinkedFastStrMap = buildEachIMap(VC, V_KANA)

    private fun buildSulkyMap(): LinkedFastStrMap = buildLinkedFastMap {
        SULKY_KANA.forEachIndexed { i, row ->
            val consonant = SULKY_CONSONANTS.elementAtOrNull(i)?.toString() ?: ""

            row.forEachIndexed { j, column ->
                if (column != null) {
                    val vowel = VOWELS.elementAtOrNull(j) ?: return@forEachIndexed
                    val set = get(column) ?: objectOpenSetOf()
                    if (column.contains("ゔ"))
                        set.add("$consonant$vowel")
                    else {
                        set.add("${consonant}y$vowel")
                        set.add("${consonant}h$vowel")
                    }

                    put(column, set)
                }
            }
        }
    }

    private fun buildSmallKanaMap(): LinkedFastStrMap = buildLinkedFastMap {
        SMALL_KANA.forEach { (kana, roman) ->
            val processedSet = objectOpenSetOf<String>().apply {
                add("x$roman")
                add("l$roman")
            }
            put(kana, processedSet)
        }
    }

    private fun buildSidetoneMap(m: LinkedFastStrMap): LinkedFastStrMap = buildLinkedFastMap {
        val tsuSet = m["つ"] ?: return@buildLinkedFastMap

        val sidetoneSet = objectOpenSetOf<String>().apply {
            tsuSet.forEach { roman ->
                add("x$roman")
                add("l$roman")
            }
        }

        put("っ", sidetoneSet)
    }

    private fun addHepburnMap(m: LinkedFastStrMap) {
        HEPBURN_RULES.forEach { (k, v) ->
            m.computeIfAbsent(k) { objectOpenSetOf() }.addAll(v)
        }
    }

    private fun buildEachIMap(consonants: ObjectLinkedOpenHashSet<Char?>, kana: List<List<Char?>>) =
        buildLinkedFastMap {
            kana.forEachIndexed { i, row ->
                val consonant = consonants.elementAtOrNull(i)?.toString() ?: ""

                row.forEachIndexed { j, column ->
                    if (column != null) {
                        val vowel = VOWELS.elementAtOrNull(j) ?: return@forEachIndexed
                        put(column.toString(), objectOpenSetOf("$consonant$vowel"))
                    }
                }
            }
        }

    private companion object {
        private const val CACHE_NAME = "MAPCACHE.gz.dat"
        private val cacheManager = CacheManager()

        private val VOWELS = objectLinkedOpenSetOf('a', 'i', 'u', 'e', 'o')
        private val CONSONANTS = objectLinkedOpenSetOf(null, 'k', 's', 't', 'n', 'h', 'm', 'y', 'r', 'w')
        private val N_CONSONANTS = objectOpenSetOf("n", "nn", "n'")
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

        private val VC: ObjectLinkedOpenHashSet<Char?> =
            objectLinkedOpenSetOf('g', 'z', 'd', 'b', 'p')
        private val V_KANA = listOf<List<Char>>(
            listOf('が', 'ぎ', 'ぐ', 'げ', 'ご'),
            listOf('ざ', 'じ', 'ず', 'ぜ', 'ぞ'),
            listOf('だ', 'ぢ', 'づ', 'で', 'ど'),
            listOf('ば', 'び', 'ぶ', 'べ', 'ぼ'),
            listOf('ぱ', 'ぴ', 'ぷ', 'ぺ', 'ぽ')
        )

        private val SULKY_CONSONANTS = objectLinkedOpenSetOf('k', 's', 't', 'n', 'h', 'm', 'r', 'w', 'v')
        private val SULKY_KANA = listOf<List<String?>>(
            listOf("きゃ", null, "きゅ", "きぇ", "きょ"),
            listOf("しゃ", null, "しゅ", "しぇ", "しょ"),
            listOf("ちゃ", null, "ちゅ", "ちぇ", "ちょ"),
            listOf("にゃ", null, "にゅ", "にぇ", "にょ"),
            listOf("ひゃ", null, "ひゅ", "ひぇ", "ひょ"),
            listOf("みゃ", null, "みゅ", "みぇ", "みょ"),
            listOf("りゃ", null, "りゅ", "りぇ", "りょ"),
            listOf(null, "うぃ", null, "うぇ", null),
            listOf("ゔぁ", "ゔぃ", "ゔ", "ゔぇ", "ゔぉ")
        )

        private val SMALL_KANA = listOf(
            "ぁ" to "a",
            "ぃ" to "i",
            "ぅ" to "u",
            "ぇ" to "e",
            "ぉ" to "o",
            "ゃ" to "ya",
            "ゅ" to "yu",
            "ょ" to "yo"
        )

        private val HEPBURN_RULES = linkedFastMapOf(
            "し" to objectOpenSetOf("shi"),
            "ち" to objectOpenSetOf("chi"),
            "つ" to objectOpenSetOf("tsu"),
            "じ" to objectOpenSetOf("ji"),
            "ふぁ" to objectOpenSetOf("fa"),
            "ふぃ" to objectOpenSetOf("fi"),
            "ふ" to objectOpenSetOf("fu"),
            "ふぇ" to objectOpenSetOf("fe"),
            "ふぉ" to objectOpenSetOf("fo"),
            "ちゃ" to objectOpenSetOf("cya", "cha"),
            "ちゅ" to objectOpenSetOf("cyu", "chu"),
            "ちぇ" to objectOpenSetOf("cye", "che"),
            "ちょ" to objectOpenSetOf("cyo", "cho")
        )
    }
}