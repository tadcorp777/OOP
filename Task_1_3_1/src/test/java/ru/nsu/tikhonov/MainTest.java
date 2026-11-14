
package ru.nsu.tikhonov;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки главного класса программы.
 */
class MainTest {

    @TempDir
    private Path tempDir;

    /**
     * Выполнение поиска и вывод результата в консоль.
     */
    @Test
    void printsFoundIndexes() throws IOException {
        Path file = createFile("abracadabra");

        String output = runMain(file.toString(), "bra");

        assertTrue(output.contains("Найденные индексы: [1, 8]"));
    }

    /**
     * Вывод сообщения при отсутствии файла.
     */
    @Test
    void printsErrorWhenFileDoesNotExist() {
        Path file = tempDir.resolve("missing.txt");

        String output = runMain(file.toString(), "test");

        assertTrue(output.contains("Ошибка при чтении файла:"));
    }

    /**
     * Вывод сообщения при пустой искомой строке.
     */
    @Test
    void printsErrorForEmptySubstring() throws IOException {
        Path file = createFile("text");

        String output = runMain(file.toString(), "");

        assertTrue(output.contains(
                "Ошибка: Искомая строка не должна быть пустой"
        ));
    }

    /**
     * Запускает главный метод с заданными входными данными
     * и возвращает содержимое консольного вывода.
     */
    private String runMain(String fileName, String target) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream testOutput = new PrintStream(
                output, true, StandardCharsets.UTF_8
        );
        try {
            String input = fileName + "\n" + target + "\n";
            System.setIn(new ByteArrayInputStream(
                    input.getBytes(StandardCharsets.UTF_8)
            ));
            System.setOut(testOutput);
            Main.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
            testOutput.close();
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    /**
     * Создаёт временный файл с заданным содержимым.
     */
    private Path createFile(String content) throws IOException {
        Path file = tempDir.resolve("test.txt");

        Files.writeString(file, content, StandardCharsets.UTF_8);

        return file;
    }
}
