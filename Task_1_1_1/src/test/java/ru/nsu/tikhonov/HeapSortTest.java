package ru.nsu.tikhonov;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Тесты на heap sort. Граничные случаи и случайные массивы.
 */
class HeapSortTest {

    /**
     * Базовый пример из задания.
     */
    @Test
    void sortsExampleFromTask() {
        int[] actual = {5, 4, 3, 2, 1};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, actual);
    }

    /**
     * Пустой массив не должен падать.
     */
    @Test
    void sortsEmptyArray() {
        int[] actual = {};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {}, actual);
    }

    /**
     * Один элемент, массив должен остаться тем же.
     */
    @Test
    void sortsSingleElement() {
        int[] actual = {42};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {42}, actual);
    }

    /**
     * Уже отсортированный массив.
     */
    @Test
    void sortsAlreadySortedArray() {
        int[] actual = {1, 2, 3, 4, 5, 6};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6}, actual);
    }

    /**
     * Обратный порядок.
     */
    @Test
    void sortsReverseSortedArray() {
        int[] actual = {6, 5, 4, 3, 2, 1};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {1, 2, 3, 4, 5, 6}, actual);
    }

    /**
     * Дубликаты не должны теряться или путать порядок.
     */
    @Test
    void sortsArrayWithDuplicates() {
        int[] actual = {4, 1, 4, 2, 1, 4, 2};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {1, 1, 2, 2, 4, 4, 4}, actual);
    }

    /**
     * Отрицательные числа и ноль.
     */
    @Test
    void sortsNegativeAndPositiveNumbers() {
        int[] actual = {0, -5, 3, -1, 8, -8, 2};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {-8, -5, -1, 0, 2, 3, 8}, actual);
    }

    /**
     * Стресс-тест из 500 случайных массивов, результат сверяем со стандартной сортировкой.
     */
    @Test
    void sortsManyRandomArrays() {
        Random random = new Random(12345);

        for (int test = 0; test < 500; test++) {
            int size = random.nextInt(100);
            int[] actual = new int[size];

            for (int i = 0; i < size; i++) {
                actual[i] = random.nextInt(201) - 100;
            }

            int[] expected = actual.clone();
            Arrays.sort(expected);

            HeapSort.heapsort(actual);

            assertArrayEquals(expected, actual, "Failed on test #" + test);
        }
    }

    /**
     * Проверяем, что сортировка меняет сам массив.
     */
    @Test
    void sortsInPlace() {
        int[] actual = {3, 1, 2};

        HeapSort.heapsort(actual);

        assertArrayEquals(new int[] {1, 2, 3}, actual);
    }

    /**
     * Некорректный аргумент null.
     */
    @Test
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> HeapSort.heapsort(null));
    }
}