package ru.nsu.tikhonov.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация ориентированного графа на основе матрицы инцидентности.
 */
public class IncidenceMatrixGraph extends AbstractGraph {

    private List<Edge> edges = new ArrayList<>();
    private int[][] incidenceMatrix = new int[0][0];

    @Override
    public void addVertex(String vertex) {
        checkVertexCanBeAdded(vertex);

        int oldSize = vertices.size();
        int[][] newMatrix = new int[oldSize + 1][edges.size()];

        for (int i = 0; i < oldSize; i++) {
            for (int j = 0; j < edges.size(); j++) {
                newMatrix[i][j] = incidenceMatrix[i][j];
            }
        }

        vertices.add(vertex);
        incidenceMatrix = newMatrix;
    }

    @Override
    public void removeVertex(String vertex) {
        int vertexIndex = getVertexIndex(vertex);
        int oldSize = vertices.size();

        List<Edge> newEdges = new ArrayList<>();

        for (Edge edge : edges) {
            if (!edge.getFrom().equals(vertex)
                    && !edge.getTo().equals(vertex)) {
                newEdges.add(edge);
            }
        }

        int[][] newMatrix =
                new int[oldSize - 1][newEdges.size()];

        int newRow = 0;

        for (int i = 0; i < oldSize; i++) {
            if (i == vertexIndex) {
                continue;
            }

            int newColumn = 0;

            for (int j = 0; j < edges.size(); j++) {
                Edge edge = edges.get(j);

                if (edge.getFrom().equals(vertex)
                        || edge.getTo().equals(vertex)) {
                    continue;
                }

                newMatrix[newRow][newColumn] =
                        incidenceMatrix[i][j];

                newColumn++;
            }

            newRow++;
        }

        vertices.remove(vertexIndex);
        edges = newEdges;
        incidenceMatrix = newMatrix;
    }

    @Override
    public void addEdge(String from, String to) {
        int fromIndex = getVertexIndex(from);
        int toIndex = getVertexIndex(to);

        if (containsEdge(from, to)) {
            return;
        }

        int oldEdgeCount = edges.size();

        int[][] newMatrix =
                new int[vertices.size()][oldEdgeCount + 1];

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < oldEdgeCount; j++) {
                newMatrix[i][j] = incidenceMatrix[i][j];
            }
        }

        if (fromIndex != toIndex) {
            newMatrix[fromIndex][oldEdgeCount] = -1;
            newMatrix[toIndex][oldEdgeCount] = 1;
        }

        edges.add(new Edge(from, to));
        incidenceMatrix = newMatrix;
    }

    @Override
    public void removeEdge(String from, String to) {
        getVertexIndex(from);
        getVertexIndex(to);

        int edgeIndex = -1;

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (edge.getFrom().equals(from)
                    && edge.getTo().equals(to)) {
                edgeIndex = i;
                break;
            }
        }

        if (edgeIndex == -1) {
            return;
        }

        List<Edge> newEdges = new ArrayList<>(edges);
        newEdges.remove(edgeIndex);

        int[][] newMatrix =
                new int[vertices.size()][newEdges.size()];

        for (int i = 0; i < vertices.size(); i++) {
            int newColumn = 0;

            for (int j = 0; j < edges.size(); j++) {
                if (j == edgeIndex) {
                    continue;
                }

                newMatrix[i][newColumn] = incidenceMatrix[i][j];
                newColumn++;
            }
        }

        edges = newEdges;
        incidenceMatrix = newMatrix;
    }

    @Override
    public List<String> getNeighbors(String vertex) {
        int vertexIndex = getVertexIndex(vertex);
        List<String> neighbors = new ArrayList<>();

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (incidenceMatrix[vertexIndex][i] == -1
                    || (edge.getFrom().equals(vertex)
                    && edge.getTo().equals(vertex))) {
                neighbors.add(edge.getTo());
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

        for (int i = 0; i < edges.size(); i++) {
            Edge edge = edges.get(i);

            if (from.equals(to)
                    && edge.getFrom().equals(from)
                    && edge.getTo().equals(to)) {
                return true;
            }

            if (incidenceMatrix[fromIndex][i] == -1
                    && incidenceMatrix[toIndex][i] == 1) {
                return true;
            }
        }

        return false;
    }
}
