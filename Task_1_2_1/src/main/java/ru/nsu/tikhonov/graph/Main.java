package ru.nsu.tikhonov.graph;

import java.io.IOException;
import java.util.List;
import ru.nsu.tikhonov.graph.algorithms.DepthFirstTopologicalSort;
import ru.nsu.tikhonov.graph.algorithms.KahnTopologicalSort;
import ru.nsu.tikhonov.graph.algorithms.TopologicalSort;

/**
 * Точка входа для демонстрации работы графа и алгоритмов
 * топологической сортировки.
 */
public class Main {

    /**
     * Читает граф из файла и выводит результаты сортировок.
     */
    public static void main(String[] args) {
        Graph graph = new AdjacencyListGraph();

        try {
            graph.readFromFile("graph.txt");

            System.out.println("Граф:");
            System.out.println(graph);

            TopologicalSort kahnSort = new KahnTopologicalSort();
            List<String> kahnResult = kahnSort.sort(graph);

            System.out.println("Сортировка по алгоритму Кана: " + kahnResult);

            TopologicalSort depthFirstSort =
                    new DepthFirstTopologicalSort();

            List<String> depthFirstResult =
                    depthFirstSort.sort(graph);

            System.out.println("Сортировка по DFS: " + depthFirstResult);

        } catch (IOException e) {
            System.out.println(
                    "Ошибка чтения файла: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Ошибка обработки графа: " + e.getMessage());
        }
    }
}
