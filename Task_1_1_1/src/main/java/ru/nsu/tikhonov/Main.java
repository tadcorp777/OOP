package ru.nsu.tikhonov;

import java.util.Arrays;

/**
 * Демонстрирует работу пирамидальной сортировки
 * и роверку её теоретической сложности.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        /**
         * Массив по убыванию, как худший случай.
         */
        int[] array = {5, 4, 3, 2, 1};

        System.out.println("Before: " + Arrays.toString(array));
        HeapSort.heapsort(array);
        System.out.println("After:  " + Arrays.toString(array));

        runComplexityDemo();
    }

    /**
     * Замеряем время работы на разных размерах, чтобы показать O(n log n).
     */
    private static void runComplexityDemo() {
        System.out.println();
        System.out.println("Complexity demonstration (average of 5 runs):");
        System.out.printf("%10s %15s %15s%n", "n", "time, ms", "n*log2(n)");

        int[] sizes = {10_000, 20_000, 40_000, 80_000, 160_000};

        int[] warmup = createDescendingArray(20_000);
        HeapSort.heapsort(warmup);

        for (int size : sizes) {
            long totalNanos = 0;

            for (int run = 0; run < 5; run++) {
                int[] data = createDeterministicArray(size);

                long start = System.nanoTime();
                HeapSort.heapsort(data);
                long finish = System.nanoTime();

                totalNanos += finish - start;
            }

            double averageMs = totalNanos / 5_000_000.0;
            double nLogN = size * (Math.log(size) / Math.log(2));

            System.out.printf("%10d %15.3f %15.0f%n", size, averageMs, nLogN);
        }

        System.out.println();
        System.out.println("При увеличении n в 2 раза время должно расти примерно в 2 раза,");
        System.out.println("что соответствует O(n log n), с поправкой на JVM и особенности компьютера.");
    }

    /**
     * Массив по убыванию в качестве стресс-теста для сортировки.
     */
    private static int[] createDescendingArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        return array;
    }

    /**
     * Генератор псевдослучайных чисел.
     */
    private static int[] createDeterministicArray(int size) {
        int[] array = new int[size];
        long value = 0x1234ABCDL;

        for (int i = 0; i < size; i++) {
            value = (value * 1_103_515_245L + 12_345L) & 0x7FFFFFFFL;
            array[i] = (int) value;
        }

        return array;
    }
}