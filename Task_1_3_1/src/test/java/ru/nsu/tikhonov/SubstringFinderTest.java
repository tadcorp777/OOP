package ru.nsu.tikhonov;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки алгоритма поиска подстроки.
 */
class SubstringFinderTest {

    @TempDir
    private Path tempDir;

    /**
     * Поиск нескольких вхождений подстроки.
     */
    @Test
    void findsAllOccurrences() throws IOException {
        Path file = createFile("абракадабра");

        List<Long> result = SubstringFinder.find(
                file.toString(), "бра"
        );

        assertEquals(Arrays.asList(1L, 8L), result);
    }

    /**
     * Поиск пересекающихся вхождений подстроки.
     */
    @Test
    void findsOverlappingOccurrences() throws IOException {
        Path file = createFile("ааааа");

        List<Long> result = SubstringFinder.find(
                file.toString(), "аа"
        );

        assertEquals(
                Arrays.asList(0L, 1L, 2L, 3L),
                result
        );
    }

    /**
     * Отсутствие совпадений.
     */
    @Test
    void returnsEmptyListWhenSubstringNotFound() throws IOException {
        Path file = createFile("привет");

        List<Long> result = SubstringFinder.find(
                file.toString(), "мир"
        );

        assertTrue(result.isEmpty());
    }

    /**
     * Поиск подстроки, совпадающей со всем содержимым файла.
     */
    @Test
    void findsSubstringEqualToFileContent() throws IOException {
        Path file = createFile("привет");

        List<Long> result = SubstringFinder.find(
                file.toString(), "привет"
        );

        assertEquals(Collections.singletonList(0L), result);
    }

    /**
     * Поиск подстроки, длина которой больше содержимого файла.
     */
    @Test
    void returnsEmptyListWhenSubstringIsLongerThanFile()
            throws IOException {
        Path file = createFile("кот");

        List<Long> result = SubstringFinder.find(
                file.toString(), "котик"
        );

        assertTrue(result.isEmpty());
    }

    /**
     * Работа алгоритма с пустым файлом.
     */
    @Test
    void returnsEmptyListForEmptyFile() throws IOException {
        Path file = createFile("");

        List<Long> result = SubstringFinder.find(
                file.toString(), "тест"
        );

        assertTrue(result.isEmpty());
    }

    /**
     * Обработка пустой искомой строки.
     */
    @Test
    void throwsExceptionForEmptySubstring() throws IOException {
        Path file = createFile("текст");

        assertThrows(
                IllegalArgumentException.class,
                () -> SubstringFinder.find(file.toString(), "")
        );
    }

    /**
     * Проверяет обработку null вместо искомой строки.
     */
    @Test
    void throwsExceptionForNullSubstring() throws IOException {
        Path file = createFile("текст");

        assertThrows(
                IllegalArgumentException.class,
                () -> SubstringFinder.find(file.toString(), null)
        );
    }

    /**
     * Обработка ситуации, когда файл не существует.
     */
    @Test
    void throwsIOExceptionWhenFileDoesNotExist() {
        Path file = tempDir.resolve("missing.txt");

        assertThrows(
                IOException.class,
                () -> SubstringFinder.find(file.toString(), "тест")
        );
    }

    /**
     * Поиск с однобуквенной искомой строкой.
     */
    @Test
    void findsSingleCharacterSubstring() throws IOException {
        Path file = createFile("banana");

        List<Long> result = SubstringFinder.find(
                file.toString(), "a"
        );

        assertEquals(Arrays.asList(1L, 3L, 5L), result);
    }

    /**
     * Возврат к предыдущему префиксу при несовпадении символов.
     */
    @Test
    void findsOccurrencesAfterSearchFallback() throws IOException {
        Path file = createFile("abacabab");

        List<Long> result = SubstringFinder.find(
                file.toString(), "abab"
        );

        assertEquals(Collections.singletonList(4L), result);
    }

    /**
     * Построение префиксного массива при несовпадении символов.
     */
    @Test
    void findsSubstringWithPrefixFallback() throws IOException {
        Path file = createFile("ababaca");

        List<Long> result = SubstringFinder.find(
                file.toString(), "ababaca"
        );

        assertEquals(Collections.singletonList(0L), result);
    }

    /**
     * Поиск символов разных алфавитов и Unicode-символов.
     */
    @Test
    void findsUnicodeCharacters() throws IOException {
        Path file = createFile("A😀Б😀");
        List<Long> result = SubstringFinder.find(
                file.toString(), "😀"
        );
        assertEquals(Arrays.asList(1L, 4L), result);
    }

    /**
     * Поиск подстроки в большом файле.
     */
    @Test
    void findsSubstringInLargeFile() throws IOException {
        Path file = tempDir.resolve("large.txt");

        String block = "abcdefghij";
        int repetitions = 500_000;

        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < repetitions; i++) {
                writer.write(block);
            }
            writer.write("END");
        }

        long expectedIndex = (long) repetitions * block.length();

        List<Long> result = SubstringFinder.find(
                file.toString(), "END"
        );

        assertEquals(
                Collections.singletonList(expectedIndex),
                result
        );
    }

    /**
     * Создаёт временный файл с заданным содержимым в кодировке UTF-8.
     */
    private Path createFile(String content) throws IOException {
        Path file = tempDir.resolve("test.txt");

        Files.writeString(file, content, StandardCharsets.UTF_8);

        return file;
    }
}
