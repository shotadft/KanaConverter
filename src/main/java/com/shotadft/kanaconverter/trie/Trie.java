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
package com.shotadft.kanaconverter.trie;

import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

public final class Trie {
    static class TrieNode {
        final Map<Character, TrieNode> children = new HashMap<>();
        String kana;
    }

    public static class RomaTrie {
        private final TrieNode root = new TrieNode();

        public void insert(@NonNull String roma, String kana) {
            var node = root;

            for (char c : roma.toCharArray()) {
                node = node.children.computeIfAbsent(c, _ -> new TrieNode());
            }

            node.kana = kana;
        }

        public String search(@NonNull String roma) {
            var node = root;

            for (char c : roma.toCharArray()) {
                node = node.children.get(c);

                if (node == null) return null;
            }

            return node.kana;
        }

        public boolean hasPrefix(@NonNull String prefix) {
            var node = root;

            for (char c : prefix.toCharArray()) {
                node = node.children.get(c);

                if (node == null) return false;
            }

            return true;
        }
    }
}
