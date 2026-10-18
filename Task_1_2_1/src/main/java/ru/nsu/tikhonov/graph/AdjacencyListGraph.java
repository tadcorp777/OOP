package ru.nsu.tikhonov.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация ориентированного графа на основе списка смежности.
 */
public class AdjacencyListGraph extends AbstractGraph {

    private final Map<String, List<String>> adjacencyList = new HashMap<>();

    @Override
    public void addVertex(String vertex) {
        checkVertexCanBeAdded(vertex);

        vertices.add(vertex);
        adjacencyList.put(vertex, new ArrayList<>());
    }

    @Override
    public void removeVertex(String vertex) {
        checkVertexExists(vertex);

        vertices.remove(vertex);
        adjacencyList.remove(vertex);

        for (List<String> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    @Override
    public void addEdge(String from, String to) {
        checkVertexExists(from);
        checkVertexExists(to);

        if (!containsEdge(from, to)) {
            adjacencyList.get(from).add(to);
        }
    }

    @Override
    public void removeEdge(String from, String to) {
        checkVertexExists(from);
        checkVertexExists(to);

        adjacencyList.get(from).remove(to);
    }

    @Override
    public List<String> getNeighbors(String vertex) {
        checkVertexExists(vertex);

        return new ArrayList<>(adjacencyList.get(vertex));
    }

    @Override
    public boolean containsEdge(String from, String to) {
        if (!containsVertex(from) || !containsVertex(to)) {
            return false;
        }

        return adjacencyList.get(from).contains(to);
    }
}
