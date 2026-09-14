package ru.nsu.tikhonov;

/**
 * Реализует алгоритм пирамидальной сортировки.
 * Алгоритм сортирует массив целых чисел по возрастанию
 */
public final class HeapSort {

    /**
     * Приватный конструктор, чтобы никто случайно не сделал new HeapSort().
     */
    private HeapSort() {
    }

    /**
     * Публичная точка входа. Сортирует массив по возрастанию.
     */
    public static void heapsort(int[] array) {
        if (array == null) {
            throw new NullPointerException("array must not be null");
        }

        int n = array.length;

        /**
         * Строим max-кучу. Идём от последнего родителя к корню и просеиваем вниз.
         */
        for (int i = n / 2 - 1; i >= 0; i--) {
            siftDown(array, i, n);
        }

        /**
         * По очереди вынимаем максимум в конец и восстанавливаем кучу.
         */
        for (int end = n - 1; end > 0; end--) {
            swap(array, 0, end);
            siftDown(array, 0, end);
        }
    }

    /**
     * Просеивание вниз. Двигаем элемент к листьям, пока он меньше своих детей.
     */
    private static void siftDown(int[] array, int root, int heapSize) {
        while (true) {
            int left = 2 * root + 1;
            int right = left + 1;
            int largest = root;

            /**
             * Ищем наибольшего из трёх. Родитель и два его ребёнка.
             */
            if (left < heapSize && array[left] > array[largest]) {
                largest = left;
            }

            if (right < heapSize && array[right] > array[largest]) {
                largest = right;
            }

            /**
             * Родитель уже больше детей, кучу можно не трогать.
             */
            if (largest == root) {
                return;
            }

            swap(array, root, largest);
            root = largest;
        }
    }

    /**
     * Обмен двух элементов местами.
     */
    private static void swap(int[] array, int i, int j) {
        int temporary = array[i];
        array[i] = array[j];
        array[j] = temporary;
    }
}