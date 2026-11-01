package ru.nsu.tikhonov;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки корректности работы HashTable.
 */
class HashTableTest {

    private HashTable<String, Number> hashTable;

    /**
     * Создаёт новую пустую хеш-таблицу перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    /**
     * Создание пустой таблицы.
     */
    @Test
    void testEmptyTable() {
        assertTrue(hashTable.isEmpty());
        assertEquals(0, hashTable.size());
    }

    /**
     * Добавление элемента и получение значения по ключу.
     */
    @Test
    void testPutAndGet() {
        hashTable.put("one", 1);

        assertEquals(Integer.valueOf(1), hashTable.get("one"));
        assertEquals(1, hashTable.size());
    }

    /**
     * Обновление значения существующего элемента.
     */
    @Test
    void testUpdate() {
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);

        assertEquals(Double.valueOf(1.0), hashTable.get("one"));
    }

    /**
     * Возникновение исключения при обновлении
     * элемента с отсутствующим ключом.
     */
    @Test
    void testUpdateMissingKey() {
        assertThrows(
                IllegalArgumentException.class,
                () -> hashTable.update("missing", 10)
        );
    }

    /**
     * Замена значения при добавлении уже существующего ключа.
     */
    @Test
    void testPutExistingKey() {
        hashTable.put("one", 1);
        hashTable.put("one", 2);

        assertEquals(Integer.valueOf(2), hashTable.get("one"));
        assertEquals(1, hashTable.size());
    }

    /**
     * Работа метода определения наличия ключа.
     */
    @Test
    void testContainsKey() {
        hashTable.put("one", 1);

        assertTrue(hashTable.containsKey("one"));
        assertFalse(hashTable.containsKey("two"));
    }

    /**
     * Успешное удаление элемента и повторное удаление
     * уже отсутствующей пары.
     */
    @Test
    void testRemove() {
        hashTable.put("one", 1);

        assertTrue(hashTable.remove("one", 1));
        assertFalse(hashTable.containsKey("one"));
        assertEquals(0, hashTable.size());
        assertFalse(hashTable.remove("one", 1));
    }

    /**
     * Элемент не удаляется при несовпадении значения.
     */
    @Test
    void testRemoveWithWrongValue() {
        hashTable.put("one", 1);

        assertFalse(hashTable.remove("one", 2));

        assertTrue(hashTable.containsKey("one"));
        assertEquals(Integer.valueOf(1), hashTable.get("one"));
    }

    /**
     * Возможность хранения и удаления null в качестве значения.
     */
    @Test
    void testNullValue() {
        hashTable.put("one", null);

        assertTrue(hashTable.containsKey("one"));
        assertNull(hashTable.get("one"));

        assertTrue(hashTable.remove("one", null));
        assertFalse(hashTable.containsKey("one"));
    }

    /**
     * Запрет на использование null в качестве ключа.
     */
    @Test
    void testNullKey() {
        assertThrows(
                IllegalArgumentException.class,
                () -> hashTable.put(null, 1)
        );
    }

    /**
     * Получение количества элементов и определение
     * пустоты таблицы после добавления и удаления элементов.
     */
    @Test
    void testSizeAndIsEmpty() {
        assertTrue(hashTable.isEmpty());

        hashTable.put("one", 1);
        hashTable.put("two", 2);

        assertEquals(2, hashTable.size());
        assertFalse(hashTable.isEmpty());

        hashTable.remove("one", 1);
        hashTable.remove("two", 2);

        assertEquals(0, hashTable.size());
        assertTrue(hashTable.isEmpty());
    }

    /**
     * Обработка коллизий и удаление элемента
     * из середины связного списка.
     */
    @Test
    void testCollisions() {
        HashTable<CollisionKey, Integer> table = new HashTable<>();

        CollisionKey first = new CollisionKey("first");
        CollisionKey second = new CollisionKey("second");
        CollisionKey third = new CollisionKey("third");

        table.put(first, 1);
        table.put(second, 2);
        table.put(third, 3);

        assertEquals(Integer.valueOf(1), table.get(first));
        assertEquals(Integer.valueOf(2), table.get(second));
        assertEquals(Integer.valueOf(3), table.get(third));

        assertTrue(table.remove(second, 2));

        assertNull(table.get(second));
        assertEquals(Integer.valueOf(1), table.get(first));
        assertEquals(Integer.valueOf(3), table.get(third));
    }

    /**
     * Увеличение вместимости таблицы
     * и сохранность элементов после перераспределения.
     */
    @Test
    void testResize() {
        for (int i = 0; i < 1000; i++) {
            hashTable.put("key" + i, i);
        }

        assertEquals(1000, hashTable.size());

        for (int i = 0; i < 1000; i++) {
            assertEquals(
                    Integer.valueOf(i),
                    hashTable.get("key" + i)
            );
        }
    }

    /**
     * Уменьшение вместимости таблицы после удаления
     * большинства элементов и сохранность оставшихся данных.
     */
    @Test
    void testShrink() {
        for (int i = 0; i < 1000; i++) {
            hashTable.put("key" + i, i);
        }

        for (int i = 0; i < 990; i++) {
            assertTrue(hashTable.remove("key" + i, i));
        }

        assertEquals(10, hashTable.size());

        for (int i = 990; i < 1000; i++) {
            assertEquals(
                    Integer.valueOf(i),
                    hashTable.get("key" + i)
            );
        }

        for (int i = 1000; i < 1100; i++) {
            hashTable.put("key" + i, i);
        }

        assertEquals(110, hashTable.size());

        for (int i = 990; i < 1100; i++) {
            assertEquals(
                    Integer.valueOf(i),
                    hashTable.get("key" + i)
            );
        }
    }

    /**
     * Перебор всех элементов таблицы с помощью итератора.
     */
    @Test
    void testIteration() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        Map<String, Number> result = new HashMap<>();

        for (HashTableEntry<String, Number> entry : hashTable) {
            result.put(entry.getKey(), entry.getValue());
        }

        assertEquals(3, result.size());
        assertEquals(Integer.valueOf(1), result.get("one"));
        assertEquals(Integer.valueOf(2), result.get("two"));
        assertEquals(Integer.valueOf(3), result.get("three"));
    }

    /**
     * Возникновение ConcurrentModificationException
     * при добавлении элемента во время итерации.
     */
    @Test
    void testConcurrentModificationAfterPut() {
        hashTable.put("one", 1);

        Iterator<HashTableEntry<String, Number>> iterator =
                hashTable.iterator();

        hashTable.put("two", 2);

        assertThrows(
                ConcurrentModificationException.class,
                iterator::hasNext
        );
    }

    /**
     * Возникновение ConcurrentModificationException
     * при удалении элемента во время итерации.
     */
    @Test
    void testConcurrentModificationAfterRemove() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        Iterator<HashTableEntry<String, Number>> iterator =
                hashTable.iterator();

        hashTable.remove("one", 1);

        assertThrows(
                ConcurrentModificationException.class,
                iterator::next
        );
    }

    /**
     * Возможность обновления значения элемента
     * без нарушения работы уже созданного итератора.
     */
    @Test
    void testUpdateDuringIteration() {
        hashTable.put("one", 1);

        Iterator<HashTableEntry<String, Number>> iterator =
                hashTable.iterator();

        hashTable.update("one", 2);

        HashTableEntry<String, Number> entry = iterator.next();

        assertEquals("one", entry.getKey());
        assertEquals(Integer.valueOf(2), entry.getValue());
    }

    /**
     * Сравнение равных таблиц и совпадение их хеш-кодов.
     */
    @Test
    void testEquals() {
        HashTable<String, Number> first = new HashTable<>();
        HashTable<String, Number> second = new HashTable<>();

        first.put("one", 1);
        first.put("two", 2);

        second.put("two", 2);
        second.put("one", 1);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    /**
     * Определение неравенства таблиц с разными значениями.
     */
    @Test
    void testNotEquals() {
        HashTable<String, Number> first = new HashTable<>();
        HashTable<String, Number> second = new HashTable<>();

        first.put("one", 1);
        second.put("one", 2);

        assertNotEquals(first, second);
    }

    /**
     * Формирование строкового представления таблицы.
     */
    @Test
    void testToString() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        String result = hashTable.toString();

        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains("one=1"));
        assertTrue(result.contains("two=2"));
    }

    /**
     * Представляет ключ, у которого всегда одинаковый хеш-код.
     */
    private static class CollisionKey {

        private final String value;

        /**
         * Создаёт ключ с заданным значением.
         */
        public CollisionKey(String value) {
            this.value = value;
        }

        /**
         * Возвращает постоянный хеш-код для создания коллизий.
         */
        @Override
        public int hashCode() {
            return 1;
        }

        /**
         * Сравнивает ключи по их значениям.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof CollisionKey)) {
                return false;
            }

            CollisionKey other = (CollisionKey) obj;

            return value.equals(other.value);
        }
    }
}