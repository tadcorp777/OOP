package ru.nsu.tikhonov;

/**
 * Пара ключ-значение, хранящаяся в хеш-таблице.
 */
public class HashTableEntry<K, V> {

    private final K key;

    private final V value;

    /**
     * Создаёт новую пару ключ-значение.
     */
    public HashTableEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Возвращает ключ элемента.
     */
    public K getKey() {
        return key;
    }

    /**
     * Возвращает значение элемента.
     */
    public V getValue() {
        return value;
    }
}