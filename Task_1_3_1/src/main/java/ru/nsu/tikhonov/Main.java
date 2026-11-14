package ru.nsu.tikhonov;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Главный класс программы для проверки поиска подстроки.
 */
public class Main {

    /**
     * Запускает поиск подстроки в файле и выводит найденные индексы.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Введите имя файла: ");
        String fileName = input.nextLine();
        System.out.print("Введите искомую подстроку: ");
        String target = input.nextLine();

        try {
            List<Long> result = SubstringFinder.find(fileName, target);
            System.out.println("Найденные индексы: " + result);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
