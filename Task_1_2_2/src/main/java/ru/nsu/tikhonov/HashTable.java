package ru.nsu.tikhonov;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Хеш-таблица для хранения пар ключ-значение.
 * Для разрешения коллизий используется метод цепочек.
 */
public class HashTable<K, V> implements Iterable<HashTableEntry<K, V>> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final double MIN_LOAD_FACTOR = 0.25;

    private Node<K, V>[] buckets;

    private int size;
    private int modCount;

    /**
     * Создаёт пустую хеш-таблицу с начальной вместимостью.
     */
    public HashTable() {
        buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
        modCount = 0;
    }

    /**
     * Представляет узел связного списка, хранящий ключ, значение
     * и ссылку на следующий узел.
     */
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        /**
         * Создаёт новый узел.
         */
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    /**
     * Вычисляет индекс ячейки массива для заданного ключа.
     */
    private int getBucketIndex(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return Math.floorMod(key.hashCode(), buckets.length);
    }

    /**
     * Ищет узел с заданным ключом.
     */
    private Node<K, V> findNode(Object key) {
        int index = getBucketIndex(key);

        Node<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    /**
     * Добавляет пару ключ-значение в таблицу.
     * Если ключ уже существует, заменяет его значение.
     */
    public void put(K key, V value) {
        Node<K, V> existingNode = findNode(key);

        if (existingNode != null) {
            existingNode.value = value;
            return;
        }

        if (size + 1 > buckets.length * LOAD_FACTOR) {
            resize(buckets.length * 2);
        }

        int index = getBucketIndex(key);

        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;

        size++;
        modCount++;
    }

    /**
     * Возвращает значение по заданному ключу.
     */
    public V get(K key) {
        Node<K, V> node = findNode(key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    /**
     * Обновляет значение существующего элемента.
     */
    public void update(K key, V value) {
        Node<K, V> node = findNode(key);

        if (node == null) {
            throw new IllegalArgumentException("Key not found: " + key);
        }

        node.value = value;
    }

    /**
     * Проверяет наличие элемента с заданным ключом.
     */
    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    /**
     * Удаляет пару ключ-значение из таблицы.
     * Удаление происходит только при совпадении ключа и значения.
     */
    public boolean remove(K key, V value) {
        int index = getBucketIndex(key);

        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)
                    && (current.value == null
                    ? value == null
                    : current.value.equals(value))) {

                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;
                modCount++;

                shrink();

                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;
    }

    /**
     * Возвращает количество элементов в таблице.
     */
    public int size() {
        return size;
    }

    /**
     * Проверяет, является ли таблица пустой.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Изменяет вместимость массива и перераспределяет
     * существующие узлы по новым ячейкам.
     */
    private void resize(int newCapacity) {
        Node<K, V>[] newBuckets =
                (Node<K, V>[]) new Node[newCapacity];

        for (int i = 0; i < buckets.length; i++) {
            Node<K, V> current = buckets[i];

            while (current != null) {
                Node<K, V> next = current.next;

                int newIndex = Math.floorMod(
                        current.key.hashCode(),
                        newBuckets.length
                );

                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;

                current = next;
            }
        }

        buckets = newBuckets;
    }

    /**
     * Создаёт итератор для перебора элементов хеш-таблицы.
     */
    @Override
    public Iterator<HashTableEntry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Реализует перебор элементов хеш-таблицы.
     * Отслеживает структурные изменения таблицы во время итерации.
     */
    private class HashTableIterator implements Iterator<HashTableEntry<K, V>> {

        private final int expectedModCount;
        private int bucketIndex;
        private Node<K, V> nextNode;

        /**
         * Создаёт итератор и находит первый доступный элемент.
         */
        public HashTableIterator() {
            expectedModCount = modCount;
            bucketIndex = 0;
            nextNode = null;

            moveToNextNode();
        }

        /**
         * Проверяет, не изменялась ли структура таблицы
         * после создания итератора.
         */
        private void checkForModification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException(
                        "HashTable was modified during iteration"
                );
            }
        }

        /**
         * Находит следующий доступный узел, пропуская пустые ячейки массива.
         */
        private void moveToNextNode() {
            while (nextNode == null && bucketIndex < buckets.length) {
                nextNode = buckets[bucketIndex];
                bucketIndex++;
            }
        }

        /**
         * Проверяет наличие следующего элемента.
         */
        @Override
        public boolean hasNext() {
            checkForModification();

            return nextNode != null;
        }

        /**
         * Возвращает следующий элемент таблицы и перемещает итератор вперёд.
         */
        @Override
        public HashTableEntry<K, V> next() {
            checkForModification();

            if (nextNode == null) {
                throw new NoSuchElementException();
            }

            Node<K, V> current = nextNode;

            nextNode = nextNode.next;
            moveToNextNode();

            return new HashTableEntry<>(current.key, current.value);
        }
    }

    /**
     * Сравнивает текущую таблицу с другим объектом.
     * Таблицы считаются равными, если содержат одинаковые пары ключ-значение.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof HashTable<?, ?>)) {
            return false;
        }

        HashTable<?, ?> otherTable = (HashTable<?, ?>) obj;

        if (size != otherTable.size) {
            return false;
        }

        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;

            while (current != null) {
                Node<?, ?> otherNode = otherTable.findNode(current.key);

                if (otherNode == null) {
                    return false;
                }

                if (current.value == null) {
                    if (otherNode.value != null) {
                        return false;
                    }
                } else if (!current.value.equals(otherNode.value)) {
                    return false;
                }

                current = current.next;
            }
        }

        return true;
    }

    /**
     * Вычисляет хеш-код таблицы на основе хеш-кодов её пар ключ-значение.
     */
    @Override
    public int hashCode() {
        int result = 0;

        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;

            while (current != null) {
                int keyHash = current.key.hashCode();

                int valueHash = current.value == null
                        ? 0
                        : current.value.hashCode();

                result += keyHash ^ valueHash;

                current = current.next;
            }
        }

        return result;
    }

    /**
     * Формирует строковое представление таблицы в формате {key=value}.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");

        boolean first = true;

        for (HashTableEntry<K, V> entry : this) {
            if (!first) {
                result.append(", ");
            }

            result.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());

            first = false;
        }

        result.append("}");

        return result.toString();
    }

    /**
     * Уменьшает вместимость массива в два раза,
     * если заполненность таблицы стала меньше минимального коэффициента.
     * Не позволяет вместимости стать меньше начального размера.
     */
    private void shrink() {
        if (buckets.length > DEFAULT_CAPACITY
                && size <= buckets.length * MIN_LOAD_FACTOR) {

            resize(buckets.length / 2);
        }
    }
}