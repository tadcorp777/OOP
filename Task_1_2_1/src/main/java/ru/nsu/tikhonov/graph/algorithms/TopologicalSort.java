package ru.nsu.tikhonov.graph.algorithms;

import java.util.List;

import ru.nsu.tikhonov.graph.Graph;

/**
 * Интерфейс алгоритма топологической сортировки.
 */
public interface TopologicalSort {

    List<String> sort(Graph graph);
}
