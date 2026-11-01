package ru.nsu.tikhonov;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Проверяет работу пользовательского интерфейса Main.
 */
class MainTest {

    /**
     * Обработка некорректного пункта меню
     * и завершение программы.
     */
    @Test
    void testInvalidChoiceAndExit() {
        String output = runMain("wrong\n99\n0\n");

        assertTrue(output.contains("Введите целое число."));
        assertTrue(output.contains("Такого пункта меню нет."));
        assertTrue(output.contains("Завершение программы."));
    }

    /**
     * Создание таблиц, вывод списка
     * и переключение между таблицами.
     */
    @Test
    void testTableManagement() {
        String output = runMain(
                "1\n" +
                        "3\n" +
                        "2\n1\n" +
                        "2\n99\n" +
                        "2\n2\n" +
                        "0\n"
        );

        assertTrue(output.contains("Создана новая таблица #2"));
        assertTrue(output.contains("#1"));
        assertTrue(output.contains("#2 (активная)"));
        assertTrue(output.contains("Переключение на таблицу #1 выполнено."));
        assertTrue(output.contains("Таблицы с таким номером не существует."));
        assertTrue(output.contains("Переключение на таблицу #2 выполнено."));
    }

    /**
     * Основные операции с элементами активной таблицы.
     */
    @Test
    void testHashTableOperations() {
        String output = runMain(
                "4\none\n1\n" +
                        "10\n" +
                        "9\n" +
                        "11\n" +
                        "12\n" +
                        "5\none\n" +
                        "6\none\n2.5\n" +
                        "5\none\n" +
                        "8\none\n" +
                        "8\nmissing\n" +
                        "6\nmissing\n5\n" +
                        "7\none\n99\n" +
                        "7\none\n2.5\n" +
                        "7\none\n2.5\n" +
                        "10\n" +
                        "9\n" +
                        "11\n" +
                        "12\n" +
                        "0\n"
        );

        assertTrue(output.contains("Элемент добавлен."));
        assertTrue(output.contains("one = 1"));
        assertTrue(output.contains("{one=1}"));
        assertTrue(output.contains("Количество элементов: 1"));
        assertTrue(output.contains("Таблица не пуста."));
        assertTrue(output.contains("Значение: 1"));
        assertTrue(output.contains("Значение обновлено."));
        assertTrue(output.contains("Значение: 2.5"));
        assertTrue(output.contains("Ключ присутствует в таблице."));
        assertTrue(output.contains("Ключ отсутствует в таблице."));
        assertTrue(output.contains("Обновление невозможно."));
        assertTrue(output.contains("Такая пара не найдена."));
        assertTrue(output.contains("Пара успешно удалена."));
        assertTrue(output.contains("Таблица пуста."));
        assertTrue(output.contains("{}"));
        assertTrue(output.contains("Количество элементов: 0"));
    }

    /**
     * Считывание Integer, Long и Double,
     * а также повторный ввод при некорректном значении.
     */
    @Test
    void testNumberInput() {
        String output = runMain(
                "4\ninteger\n42\n" +
                        "4\nlong\n2147483648\n" +
                        "4\ndouble\n3.14\n" +
                        "4\nretry\ninvalid\n7\n" +
                        "9\n" +
                        "0\n"
        );

        assertTrue(output.contains("Введите корректное число."));
        assertTrue(output.contains("integer=42"));
        assertTrue(output.contains("long=2147483648"));
        assertTrue(output.contains("double=3.14"));
        assertTrue(output.contains("retry=7"));
    }

    /**
     * Сравнение одинаковых и различных таблиц,
     * а также обработку некорректного номера таблицы.
     */
    @Test
    void testCompareTables() {
        String output = runMain(
                "4\nshared\n1\n" +
                        "1\n" +
                        "4\nshared\n1\n" +
                        "13\n1\n2\n" +
                        "6\nshared\n2\n" +
                        "13\n1\n2\n" +
                        "13\n1\n3\n" +
                        "0\n"
        );

        assertTrue(output.contains("Таблицы равны."));
        assertTrue(output.contains("Таблицы не равны."));
        assertTrue(output.contains(
                "Одна из указанных таблиц не существует."
        ));
    }

    /**
     * Запускает Main с переданными входными данными
     * и возвращает весь вывод программы.
     */
    private String runMain(String input) {
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream testOutput = new PrintStream(
                output,
                true,
                StandardCharsets.UTF_8
        );

        try {
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
}