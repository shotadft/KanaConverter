package com.shotadft.kanaconverter.test;

import com.shotadft.kanaconverter.KanaConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

public class TestClass {
    static void main(String[] args) throws IOException {
        String input = IO.readln();
        KanaConverter converter = new KanaConverter();

        String result = converter.convert(input);

        File file = new File(System.getProperty("user.home") + "/" + Instant.now().toEpochMilli() + "-latest.log");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(input);
            writer.write('\n');
            writer.write(result);
        }

        IO.println(result);
    }
}
