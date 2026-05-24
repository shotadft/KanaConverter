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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClass {
    @Test
    @DisplayName("ローマ字 -> ひらがな テスト")
    public void convertTest() throws IOException {
        String input = "irohanihoheto tirinuruwo wakayotareso tunenaramu einookuyama kefukoete asakiyumemisi eimosesu";
        KanaConverter converter = new KanaConverter();

        String result = converter.convert(input);

        File file = new File(System.getProperty("user.home") + "/" + Instant.now().toEpochMilli() + "-latest.log");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(input);
            writer.write('\n');
            writer.write(result);
        }

        IO.println(result);

        assertEquals("いろはにほへと ちりぬるを わかよたれそ つねならむ えいのおくやま けふこえて あさきゆめみし えいもせす", result);
    }
}
