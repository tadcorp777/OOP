package ru.nsu.tikhonov;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для поиска всех вхождений подстроки в текстовом файле.
 */
public class SubstringFinder {

    /**
     * Находит все вхождения заданной подстроки в файле.
     */
    public static List<Long> find(String fileName, String target) throws IOException {
        if (target == null || target.isEmpty()) {
            throw new IllegalArgumentException("Искомая строка не должна быть пустой");
        }

        char[] pattern = target.toCharArray();
        int[] prefix = buildPrefixFunction(pattern);

        List<Long> result = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(fileName), StandardCharsets.UTF_8)) {
            long textIndex = 0;
            int matched = 0;
            int currentChar;
            while ((currentChar = reader.read()) != -1) {
                while (matched > 0 && currentChar != pattern[matched]) {
                    matched = prefix[matched - 1];
                }
                if (currentChar == pattern[matched]) {
                    matched++;
                }
                if (matched == pattern.length) {
                    result.add(textIndex - pattern.length + 1);
                    matched = prefix[matched - 1];
                }
                textIndex++;
            }
        }
        return result;
    }

    /**
     * Строит префиксный массив для искомой строки.
     */
    private static int[] buildPrefixFunction(char[] pattern) {
        int[] prefix = new int[pattern.length];
        int matched = 0;
        for (int i = 1; i < pattern.length; i++) {
            while (matched > 0 && pattern[i] != pattern[matched]) {
                matched = prefix[matched - 1];
            }
            if (pattern[i] == pattern[matched]) {
                matched++;
            }
            prefix[i] = matched;
        }
        return prefix;
    }
}