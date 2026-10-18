package ru.nsu.tikhonov.graph.algorithms;

import ru.nsu.tikhonov.graph.Graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class KahnTopologicalSort implements TopologicalSort {

    @Override
    public List<String> sort(Graph graph) {
        List<String> vertices = graph.getVertices();

        Map<String, Integer> inDegree = new HashMap<>();

        for (String vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        for (String vertex : vertices) {
            for (String neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();

        for (String vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            result.add(vertex);

            for (String neighbor : graph.getNeighbors(vertex)) {
                int newDegree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, newDegree);

                if (newDegree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalArgumentException(
                    "Граф содержит цикл");
        }

        return result;
    }
}
