package ru.nsu.tikhonov;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет несколькими хеш-таблицами.
 * Позволяет создавать таблицы, переключаться между ними
 * и сравнивать их между собой.
 */
public class HashTableManager<K, V> {

    private final List<HashTable<K, V>> tables;

    private int activeTableIndex;

    /**
     * Создаёт менеджер и первую пустую хеш-таблицу.
     */
    public HashTableManager() {
        tables = new ArrayList<>();

        tables.add(new HashTable<>());
        activeTableIndex = 0;
    }

    /**
     * Создаёт новую пустую хеш-таблицу и делает её активной.
     */
    public int createTable() {
        tables.add(new HashTable<>());
        activeTableIndex = tables.size() - 1;

        return tables.size();
    }

    /**
     * Переключается на таблицу с указанным номером.
     */
    public boolean switchTable(int tableNumber) {
        if (tableNumber < 1 || tableNumber > tables.size()) {
            return false;
        }

        activeTableIndex = tableNumber - 1;

        return true;
    }

    /**
     * Возвращает текущую активную хеш-таблицу.
     */
    public HashTable<K, V> getCurrentTable() {
        return tables.get(activeTableIndex);
    }

    /**
     * Возвращает номер текущей активной таблицы.
     */
    public int getCurrentTableNumber() {
        return activeTableIndex + 1;
    }

    /**
     * Возвращает общее количество созданных таблиц.
     */
    public int getTableCount() {
        return tables.size();
    }

    /**
     * Сравнивает две таблицы на равенство.
     */
    public boolean areEqual(int firstTableNumber, int secondTableNumber) {
        HashTable<K, V> firstTable = getTable(firstTableNumber);
        HashTable<K, V> secondTable = getTable(secondTableNumber);

        return firstTable.equals(secondTable);
    }

    /**
     * Возвращает таблицу по её номеру.
     */
    private HashTable<K, V> getTable(int tableNumber) {
        if (tableNumber < 1 || tableNumber > tables.size()) {
            throw new IllegalArgumentException("Invalid table number: " + tableNumber);
        }

        return tables.get(tableNumber - 1);
    }
}