package ru.nsu.tikhonov.graph.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.nsu.tikhonov.graph.Graph;

/**
 * Топологическая сортировка на основе поиска в глубину.
 */
public class DepthFirstTopologicalSort implements TopologicalSort {

    @Override
    public List<String> sort(Graph graph) {
        List<String> vertices = graph.getVertices();
        Map<String, Integer> state = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (String vertex : vertices) {
            state.put(vertex, 0);
        }

        for (String vertex : vertices) {
            if (state.get(vertex) == 0) {
                visit(vertex, graph, state, result);
            }
        }

        Collections.reverse(result);

        return result;
    }

    private void visit(
            String vertex,
            Graph graph,
            Map<String, Integer> state,
            List<String> result) {

        state.put(vertex, 1);

        for (String neighbor : graph.getNeighbors(vertex)) {
            if (state.get(neighbor) == 1) {
                throw new IllegalArgumentException(
                        "Граф содержит цикл");
            }

            if (state.get(neighbor) == 0) {
                visit(neighbor, graph, state, result);
            }
        }

        state.put(vertex, 2);
        result.add(vertex);
    }
}
