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
package com.shotadft.kanaconverter.test;

import com.shotadft.kanaconverter.KanaConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoldenTest {
    KanaConverter converter;
    StringBuilder log = new StringBuilder();

    @BeforeEach
    void setup() {
        converter = new KanaConverter();
    }

    @AfterEach
    void tearDown() throws IOException {
        try (FileWriter writer = new FileWriter(System.getProperty("user.home") + "/kanaconverter-test.log", true)) {
            writer.write("[" + Instant.now() + "]\n");
            writer.write(log.toString());
        }
        log.setLength(0);
    }

    @Test
    void case1() {
        String input = "irohanihoheto tirinuruwo wakayotareso tunenaramu einookuyama kefukoete asakiyumemisi eimosesu";
        String result = converter.convert(input);

        log.append(input).append("\n").append(result).append("\n\n");

        assertEquals("いろはにほへと ちりぬるを わかよたれそ つねならむ えいのおくやま けふこえて あさきゆめみし えいもせす", result);
    }

    @Test
    void case2() {
        String input = "ikisugi! ikuiku n'a-! makuragadekasugimasu! yappa dokodemo inmu ga hayatterutte hakkiri wakanndesune! nna-!";
        String result = converter.convert(input);

        log.append(input).append("\n").append(result).append("\n\n");

        assertEquals("いきすぎ! いくいく んあ-! まくらがでかすぎます! やっぱ どこでも いんむ が はやってるって はっきり わかんですね! んな-!", result);
    }
}
