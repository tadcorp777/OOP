package ru.nsu.tikhonov.util;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Отвечает за чтение команд игрока из консоли.
 */
public class InputReader {
    private final Scanner scanner;

    /**
     * Создает объект для чтения из стандартного ввода.
     */
    public InputReader() {
        this(System.in);
    }

    /**
     * Создает объект для чтения из указанного потока.
     */
    public InputReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }

    /**
     * Считывает выбор игрока.
     */
    public int readPlayerChoice() {
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("0") || input.equals("1")) {
                return Integer.parseInt(input);
            }

            System.out.println(
                    "Введите 1, чтобы взять карту, или 0, чтобы остановиться.");
        }
    }

    /**
     * Считывает количество колод.
     */
    public int readDeckCount() {
        while (true) {
            String input = scanner.nextLine();

            try {
                int deckCount = Integer.parseInt(input);

                if (deckCount > 0) {
                    return deckCount;
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Введите положительное количество колод.");
                continue;
            }

            System.out.println("Введите положительное количество колод.");
        }
    }
}