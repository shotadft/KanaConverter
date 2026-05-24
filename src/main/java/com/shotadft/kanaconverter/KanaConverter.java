/**
 * Copyright 2026 Shotadft
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
package com.shotadft.kanaconverter;

import com.shotadft.kanaconverter.trie.Trie;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public final class KanaConverter {
    private final Trie.RomaTrie trie = new Trie.RomaTrie();

    public KanaConverter() {
        Map<String, String> ROMA_TO_HIRAGANA = Map.<String, String>ofEntries(
                Map.entry("a", "あ"),
                Map.entry("i", "い"),
                Map.entry("u", "う"),
                Map.entry("e", "え"),
                Map.entry("o", "お"),

                Map.entry("ka", "か"),
                Map.entry("ki", "き"),
                Map.entry("ku", "く"),
                Map.entry("ke", "け"),
                Map.entry("ko", "こ"),

                Map.entry("sa", "さ"),
                Map.entry("shi", "し"),
                Map.entry("si", "し"),
                Map.entry("su", "す"),
                Map.entry("se", "せ"),
                Map.entry("so", "そ"),

                Map.entry("ta", "た"),
                Map.entry("chi", "ち"),
                Map.entry("ti", "ち"),
                Map.entry("tsu", "つ"),
                Map.entry("tu", "つ"),
                Map.entry("te", "て"),
                Map.entry("to", "と"),

                Map.entry("na", "な"),
                Map.entry("ni", "に"),
                Map.entry("nu", "ぬ"),
                Map.entry("ne", "ね"),
                Map.entry("no", "の"),

                Map.entry("ha", "は"),
                Map.entry("hi", "ひ"),
                Map.entry("fu", "ふ"),
                Map.entry("hu", "ふ"),
                Map.entry("he", "へ"),
                Map.entry("ho", "ほ"),

                Map.entry("ma", "ま"),
                Map.entry("mi", "み"),
                Map.entry("mu", "む"),
                Map.entry("me", "め"),
                Map.entry("mo", "も"),

                Map.entry("ya", "や"),
                Map.entry("yu", "ゆ"),
                Map.entry("yo", "よ"),

                Map.entry("ra", "ら"),
                Map.entry("ri", "り"),
                Map.entry("ru", "る"),
                Map.entry("re", "れ"),
                Map.entry("ro", "ろ"),

                Map.entry("wa", "わ"),
                Map.entry("wo", "を"),

                Map.entry("ga", "が"),
                Map.entry("gi", "ぎ"),
                Map.entry("gu", "ぐ"),
                Map.entry("ge", "げ"),
                Map.entry("go", "ご"),

                Map.entry("za", "ざ"),
                Map.entry("ji", "じ"),
                Map.entry("zi", "じ"),
                Map.entry("zu", "ず"),
                Map.entry("ze", "ぜ"),
                Map.entry("zo", "ぞ"),

                Map.entry("da", "だ"),
                Map.entry("di", "ぢ"),
                Map.entry("du", "づ"),
                Map.entry("de", "で"),
                Map.entry("do", "ど"),

                Map.entry("ba", "ば"),
                Map.entry("bi", "び"),
                Map.entry("bu", "ぶ"),
                Map.entry("be", "べ"),
                Map.entry("bo", "ぼ"),

                Map.entry("pa", "ぱ"),
                Map.entry("pi", "ぴ"),
                Map.entry("pu", "ぷ"),
                Map.entry("pe", "ぺ"),
                Map.entry("po", "ぽ"),

                Map.entry("kya", "きゃ"),
                Map.entry("kyu", "きゅ"),
                Map.entry("kyo", "きょ"),

                Map.entry("gya", "ぎゃ"),
                Map.entry("gyu", "ぎゅ"),
                Map.entry("gyo", "ぎょ"),

                Map.entry("sha", "しゃ"),
                Map.entry("sya", "しゃ"),
                Map.entry("shu", "しゅ"),
                Map.entry("syu", "しゅ"),
                Map.entry("sho", "しょ"),
                Map.entry("syo", "しょ"),

                Map.entry("ja", "じゃ"),
                Map.entry("jya", "じゃ"),
                Map.entry("zya", "じゃ"),
                Map.entry("ju", "じゅ"),
                Map.entry("jyu", "じゅ"),
                Map.entry("zyu", "じゅ"),
                Map.entry("jo", "じょ"),
                Map.entry("jyo", "じょ"),
                Map.entry("zyo", "じょ"),

                Map.entry("cha", "ちゃ"),
                Map.entry("tya", "ちゃ"),
                Map.entry("chu", "ちゅ"),
                Map.entry("tyu", "ちゅ"),
                Map.entry("cho", "ちょ"),
                Map.entry("tyo", "ちょ"),

                Map.entry("nya", "にゃ"),
                Map.entry("nyu", "にゅ"),
                Map.entry("nyo", "にょ"),

                Map.entry("hya", "ひゃ"),
                Map.entry("hyu", "ひゅ"),
                Map.entry("hyo", "ひょ"),

                Map.entry("bya", "びゃ"),
                Map.entry("byu", "びゅ"),
                Map.entry("byo", "びょ"),

                Map.entry("pya", "ぴゃ"),
                Map.entry("pyu", "ぴゅ"),
                Map.entry("pyo", "ぴょ"),

                Map.entry("mya", "みゃ"),
                Map.entry("myu", "みゅ"),
                Map.entry("myo", "みょ"),

                Map.entry("rya", "りゃ"),
                Map.entry("ryu", "りゅ"),
                Map.entry("ryo", "りょ"),

                Map.entry("fa", "ふぁ"),
                Map.entry("fi", "ふぃ"),
                Map.entry("fe", "ふぇ"),
                Map.entry("fo", "ふぉ"),

                Map.entry("xa", "ぁ"),
                Map.entry("xi", "ぃ"),
                Map.entry("xu", "ぅ"),
                Map.entry("xe", "ぇ"),
                Map.entry("xo", "ぉ"),
                Map.entry("xya", "ゃ"),
                Map.entry("xyu", "ゅ"),
                Map.entry("xyo", "ょ"),

                Map.entry("la", "ぁ"),
                Map.entry("li", "ぃ"),
                Map.entry("lu", "ぅ"),
                Map.entry("le", "ぇ"),
                Map.entry("lo", "ぉ"),
                Map.entry("lya", "ゃ"),
                Map.entry("lyu", "ゅ"),
                Map.entry("lyo", "ょ")
        );

        ROMA_TO_HIRAGANA.forEach(trie::insert);
    }

    public @NonNull String convert(@NonNull String romaji) {
        StringBuilder result = new StringBuilder();

        int i = 0;

        while (i < romaji.length()) {
            if (i + 1 < romaji.length()) {
                char c1 = romaji.charAt(i);
                char c2 = romaji.charAt(i + 1);

                if (c1 == c2 && isConsonant(c1) && c1 != 'n') {
                    result.append("っ");
                    i++;
                    continue;
                }
            }

            if (romaji.charAt(i) == 'n') {
                if (i + 1 >= romaji.length()) {
                    result.append('ん');
                    i++;
                    continue;
                }

                char next = romaji.charAt(i + 1);

                if (next == 'n') {
                    result.append('ん');
                    i++;
                    continue;
                }

                if (!isVowel(next) && next != 'y') {
                    result.append('ん');

                    i += next == '\'' ? 2 : 1;
                    continue;
                }
            }

            String bestKana = null;
            int bestLen = 0;

            for (int j = i + 1; j <= romaji.length(); j++) {
                String part = romaji.substring(i, j);

                if (!trie.hasPrefix(part))
                    break;

                String kana = trie.search(part);

                if (kana != null) {
                    bestKana = kana;
                    bestLen = part.length();
                }
            }

            if (bestKana == null) {
                result.append(romaji.charAt(i++));
                continue;
            }

            result.append(bestKana);
            i += bestLen;
        }

        return result.toString();
    }

    private boolean isVowel(char c) {
        return matches("aiueo", c);
    }

    private boolean isConsonant(char c) {
        return matches("bcdfghjklmnpqrstvwxyz", c);
    }

    private boolean matches(@NonNull String list, char c) {
        return list.indexOf(c) >= 0;
    }
}
