package ru.nsu.tikhonov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки работы HashTableManager.
 */
class HashTableManagerTest {

    private HashTableManager<String, Number> manager;

    /**
     * Новый менеджер перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        manager = new HashTableManager<>();
    }

    /**
     * Создание первой таблицы при создании менеджера.
     */
    @Test
    void testInitialTable() {
        assertEquals(1, manager.getTableCount());
        assertEquals(1, manager.getCurrentTableNumber());
        assertTrue(manager.getCurrentTable().isEmpty());
    }

    /**
     * Создание новой таблицы и автоматическое переключение на неё.
     */
    @Test
    void testCreateTable() {
        int tableNumber = manager.createTable();

        assertEquals(2, tableNumber);
        assertEquals(2, manager.getTableCount());
        assertEquals(2, manager.getCurrentTableNumber());
        assertTrue(manager.getCurrentTable().isEmpty());
    }

    /**
     * Переключение между ранее созданными таблицами.
     */
    @Test
    void testSwitchTable() {
        manager.createTable();
        manager.createTable();

        assertTrue(manager.switchTable(1));
        assertEquals(1, manager.getCurrentTableNumber());

        assertTrue(manager.switchTable(2));
        assertEquals(2, manager.getCurrentTableNumber());

        assertTrue(manager.switchTable(3));
        assertEquals(3, manager.getCurrentTableNumber());
    }

    /**
     * Обработка некорректного номера таблицы
     * при переключении.
     */
    @Test
    void testSwitchToInvalidTable() {
        assertFalse(manager.switchTable(0));
        assertFalse(manager.switchTable(2));

        assertEquals(1, manager.getCurrentTableNumber());
    }

    /**
     * Независимость данных разных хеш-таблиц.
     */
    @Test
    void testTablesAreIndependent() {
        manager.getCurrentTable().put("one", 1);

        manager.createTable();
        manager.getCurrentTable().put("two", 2);

        assertTrue(manager.switchTable(1));

        assertEquals(1, manager.getCurrentTable().size());
        assertEquals(Integer.valueOf(1), manager.getCurrentTable().get("one"));
        assertFalse(manager.getCurrentTable().containsKey("two"));

        assertTrue(manager.switchTable(2));

        assertEquals(1, manager.getCurrentTable().size());
        assertEquals(Integer.valueOf(2), manager.getCurrentTable().get("two"));
        assertFalse(manager.getCurrentTable().containsKey("one"));
    }

    /**
     * Сравнение двух одинаковых таблиц,
     * заполненных в разном порядке.
     */
    @Test
    void testAreEqual() {
        manager.getCurrentTable().put("one", 1);
        manager.getCurrentTable().put("two", 2);

        manager.createTable();

        manager.getCurrentTable().put("two", 2);
        manager.getCurrentTable().put("one", 1);

        assertTrue(manager.areEqual(1, 2));
    }

    /**
     * Сравнение таблиц с различающимися значениями.
     */
    @Test
    void testAreNotEqual() {
        manager.getCurrentTable().put("one", 1);

        manager.createTable();
        manager.getCurrentTable().put("one", 2);

        assertFalse(manager.areEqual(1, 2));
    }

    /**
     * Возникновение исключения при сравнении
     * с таблицей, которой не существует.
     */
    @Test
    void testAreEqualWithInvalidTableNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> manager.areEqual(1, 2)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.areEqual(0, 1)
        );
    }
}