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

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.jspecify.annotations.NonNull;

public final class Trie {
    public static class Node {
        final Char2ObjectMap<Node> children = new Char2ObjectOpenHashMap<>();
        String kana;

        public Node next(char c) {
            return children.get(c);
        }

        public String getKana() {
            return kana;
        }
    }

    public static class RomaTrie {
        private final Node root = new Node();

        public Node getRoot() {
            return root;
        }

        public void insert(@NonNull String roma, String kana) {
            var node = root;

            for (int i = 0; i < roma.length(); i++) {
                char c = roma.charAt(i);
                node = node.children.computeIfAbsent(c, _ -> new Node());
            }

            node.kana = kana;
        }
/*
        public String search(@NonNull String roma) {
            var node = root;

            for (int i = 0; i < roma.length(); i++) {
                char c = roma.charAt(i);
                node = node.children.get(c);

                if (node == null) return null;
            }

            return node.kana;
        }

        public boolean hasPrefix(@NonNull String prefix) {
            var node = root;

            for (int i = 0; i < prefix.length(); i++) {
                char c = prefix.charAt(i);
                node = node.children.get(c);

                if (node == null) return false;
            }

            return true;
        }
 */
    }
}
