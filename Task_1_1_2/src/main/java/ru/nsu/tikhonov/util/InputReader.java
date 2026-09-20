package ru.nsu.tikhonov.util;

import java.util.Scanner;

/**
 * Отвечает за чтение команд игрока из консоли.
 */
public class InputReader {
    private final Scanner scanner;

    public InputReader() {
        scanner = new Scanner(System.in);
    }

    /**
     * Считывает выбор игрока.
     *
     */
    public int readPlayerChoice() {
        while (true) {
            String input = scanner.nextLine();

            if (input.equals("0") || input.equals("1")) {
                return Integer.parseInt(input);
            }

            System.out.println("Введите 1, чтобы взять карту, или 0, чтобы остановиться.");
        }
    }
}