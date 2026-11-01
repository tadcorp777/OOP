package ru.nsu.tikhonov;

import java.util.Scanner;

/**
 * Взаимодействие пользователя с хеш-таблицами
 * через консольное меню.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Запускает программу и обрабатывает команды пользователя.
     */
    public static void main(String[] args) {

        HashTableManager<String, Number> manager = new HashTableManager<>();

        while (true) {
            printMenu(manager);

            int choice = readInt("Выберите действие: ");

            switch (choice) {
                case 1:
                    createTable(manager);
                    break;

                case 2:
                    switchTable(manager);
                    break;

                case 3:
                    printTables(manager);
                    break;

                case 4:
                    putElement(manager.getCurrentTable());
                    break;

                case 5:
                    getElement(manager.getCurrentTable());
                    break;

                case 6:
                    updateElement(manager.getCurrentTable());
                    break;

                case 7:
                    removeElement(manager.getCurrentTable());
                    break;

                case 8:
                    checkElement(manager.getCurrentTable());
                    break;

                case 9:
                    System.out.println(manager.getCurrentTable());
                    break;

                case 10:
                    printElements(manager.getCurrentTable());
                    break;

                case 11:
                    System.out.println(
                            "Количество элементов: "
                                    + manager.getCurrentTable().size()
                    );
                    break;

                case 12:
                    if (manager.getCurrentTable().isEmpty()) {
                        System.out.println("Таблица пуста.");
                    } else {
                        System.out.println("Таблица не пуста.");
                    }
                    break;

                case 13:
                    compareTables(manager);
                    break;

                case 0:
                    System.out.println("Завершение программы.");
                    return;

                default:
                    System.out.println("Такого пункта меню нет.");
            }

            System.out.println();
        }
    }

    /**
     * Выводит меню доступных операций и номер активной таблицы.
     */
    private static void printMenu(HashTableManager<String, Number> manager) {
        System.out.println("========== HASH TABLE ==========");
        System.out.println("Текущая таблица: #" + manager.getCurrentTableNumber());
        System.out.println("--------------------------------");
        System.out.println("1. Создать новую таблицу");
        System.out.println("2. Переключиться на другую таблицу");
        System.out.println("3. Показать список таблиц");
        System.out.println("--------------------------------");
        System.out.println("4. Добавить элемент");
        System.out.println("5. Найти значение по ключу");
        System.out.println("6. Обновить значение");
        System.out.println("7. Удалить пару ключ-значение");
        System.out.println("8. Проверить наличие ключа");
        System.out.println("9. Вывести таблицу");
        System.out.println("10. Перебрать все элементы");
        System.out.println("11. Показать количество элементов");
        System.out.println("12. Проверить, пуста ли таблица");
        System.out.println("--------------------------------");
        System.out.println("13. Сравнить две таблицы");
        System.out.println("0. Выход");
        System.out.println("================================");
    }

    /**
     * Создаёт новую пустую таблицу и переключается на неё.
     */
    private static void createTable(HashTableManager<String, Number> manager) {
        int tableNumber = manager.createTable();

        System.out.println("Создана новая таблица #" + tableNumber);
        System.out.println("Теперь она является активной.");
    }

    /**
     * Переключает активную таблицу на выбранную пользователем.
     */
    private static void switchTable(HashTableManager<String, Number> manager) {
        printTables(manager);

        int tableNumber = readInt("Введите номер таблицы: ");

        if (manager.switchTable(tableNumber)) {
            System.out.println("Переключение на таблицу #" + tableNumber + " выполнено.");
        } else {
            System.out.println("Таблицы с таким номером не существует.");
        }
    }

    /**
     * Выводит список всех созданных таблиц и отмечает активную.
     */
    private static void printTables(HashTableManager<String, Number> manager) {
        System.out.println("Список созданных таблиц:");

        for (int i = 1; i <= manager.getTableCount(); i++) {
            if (i == manager.getCurrentTableNumber()) {
                System.out.println("#" + i + " (активная)");
            } else {
                System.out.println("#" + i);
            }
        }
    }

    /**
     * Запрашивает у пользователя ключ и значение
     * и добавляет их в активную хеш-таблицу.
     */
    private static void putElement(HashTable<String, Number> hashTable) {
        String key = readText("Введите ключ: ");
        Number value = readNumber("Введите значение: ");

        hashTable.put(key, value);

        System.out.println("Элемент добавлен.");
    }

    /**
     * Запрашивает ключ и выводит соответствующее ему значение.
     */
    private static void getElement(HashTable<String, Number> hashTable) {
        String key = readText("Введите ключ: ");

        if (hashTable.containsKey(key)) {
            System.out.println("Значение: " + hashTable.get(key));
        } else {
            System.out.println("Такого ключа нет.");
        }
    }

    /**
     * Запрашивает ключ и новое значение и обновляет существующий элемент.
     */
    private static void updateElement(HashTable<String, Number> hashTable) {
        String key = readText("Введите ключ: ");
        Number value = readNumber("Введите новое значение: ");

        try {
            hashTable.update(key, value);
            System.out.println("Значение обновлено.");
        } catch (IllegalArgumentException e) {
            System.out.println("Такого ключа нет. Обновление невозможно.");
        }
    }

    /**
     * Запрашивает ключ и значение и удаляет соответствующую пару
     * из активной таблицы.
     */
    private static void removeElement(HashTable<String, Number> hashTable) {
        String key = readText("Введите ключ: ");
        Number value = readNumber("Введите значение: ");

        if (hashTable.remove(key, value)) {
            System.out.println("Пара успешно удалена.");
        } else {
            System.out.println("Такая пара не найдена.");
        }
    }

    /**
     * Проверяет наличие элемента с введённым пользователем ключом.
     */
    private static void checkElement(HashTable<String, Number> hashTable) {
        String key = readText("Введите ключ: ");

        if (hashTable.containsKey(key)) {
            System.out.println("Ключ присутствует в таблице.");
        } else {
            System.out.println("Ключ отсутствует в таблице.");
        }
    }

    /**
     * Выводит все пары ключ-значение, хранящиеся в таблице.
     */
    private static void printElements(HashTable<String, Number> hashTable) {
        if (hashTable.isEmpty()) {
            System.out.println("Таблица пуста.");
            return;
        }

        System.out.println("Содержимое таблицы:");

        for (HashTableEntry<String, Number> entry : hashTable) {
            System.out.println(
                    entry.getKey() + " = " + entry.getValue()
            );
        }
    }

    /**
     * Сравнивает две ранее созданные таблицы.
     */
    private static void compareTables(HashTableManager<String, Number> manager) {
        printTables(manager);

        int firstTable = readInt("Введите номер первой таблицы: ");
        int secondTable = readInt("Введите номер второй таблицы: ");

        try {
            if (manager.areEqual(firstTable, secondTable)) {
                System.out.println("Таблицы равны.");
            } else {
                System.out.println("Таблицы не равны.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Одна из указанных таблиц не существует.");
        }
    }

    /**
     * Считывает строку из консоли.
     */
    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Считывает целое число из консоли.
     * Повторяет запрос, если введённые данные не являются целым числом.
     */
    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    /**
     * Считывает числовое значение из консоли.
     * Последовательно пытается преобразовать введённую строку
     * в Integer, Long или Double.
     */
    private static Number readNumber(String message) {
        while (true) {
            String input = readText(message).trim();

            try {
                return Integer.valueOf(input);
            } catch (NumberFormatException e) {
            }

            try {
                return Long.valueOf(input);
            } catch (NumberFormatException e) {
            }

            try {
                return Double.valueOf(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
            }
        }
    }
}