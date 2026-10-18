package ru.nsu.tikhonov.graph;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph extends AbstractGraph {

    private int[][] adjacencyMatrix = new int[0][0];

    @Override
    public void addVertex(String vertex) {
        checkVertexCanBeAdded(vertex);

        int oldSize = vertices.size();
        int[][] newMatrix = new int[oldSize + 1][oldSize + 1];

        for (int i = 0; i < oldSize; i++) {
            for (int j = 0; j < oldSize; j++) {
                newMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }

        vertices.add(vertex);
        adjacencyMatrix = newMatrix;
    }

    @Override
    public void removeVertex(String vertex) {
        int vertexIndex = getVertexIndex(vertex);
        int oldSize = vertices.size();

        int[][] newMatrix = new int[oldSize - 1][oldSize - 1];

        int newRow = 0;

        for (int i = 0; i < oldSize; i++) {
            if (i == vertexIndex) {
                continue;
            }

            int newColumn = 0;

            for (int j = 0; j < oldSize; j++) {
                if (j == vertexIndex) {
                    continue;
                }

                newMatrix[newRow][newColumn] = adjacencyMatrix[i][j];
                newColumn++;
            }

            newRow++;
        }

        vertices.remove(vertexIndex);
        adjacencyMatrix = newMatrix;
    }

    @Override
    public void addEdge(String from, String to) {
        int fromIndex = getVertexIndex(from);
        int toIndex = getVertexIndex(to);

        adjacencyMatrix[fromIndex][toIndex] = 1;
    }

    @Override
    public void removeEdge(String from, String to) {
        int fromIndex = getVertexIndex(from);
        int toIndex = getVertexIndex(to);

        adjacencyMatrix[fromIndex][toIndex] = 0;
    }

    @Override
    public List<String> getNeighbors(String vertex) {
        int vertexIndex = getVertexIndex(vertex);
        List<String> neighbors = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            if (adjacencyMatrix[vertexIndex][i] == 1) {
                neighbors.add(vertices.get(i));
            }
        }

        return neighbors;
    }

    @Override
    public boolean containsEdge(String from, String to) {
        if (!containsVertex(from) || !containsVertex(to)) {
            return false;
        }

        int fromIndex = vertices.indexOf(from);
        int toIndex = vertices.indexOf(to);

        return adjacencyMatrix[fromIndex][toIndex] == 1;
    }
}
