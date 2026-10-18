package ru.nsu.tikhonov.graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Базовый класс для реализаций графа, содержащий общую логику хранения
 * вершин и проверок.
 */
public abstract class AbstractGraph implements Graph {

    protected final List<String> vertices = new ArrayList<>();

    @Override
    public boolean containsVertex(String vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public List<String> getVertices() {
        return new ArrayList<>(vertices);
    }

    @Override
    public void readFromFile(String fileName) throws IOException {
        GraphFileReader.readFromFile(fileName, this);
    }

    protected void checkVertexCanBeAdded(String vertex) {
        if (vertex == null || vertex.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Имя вершины не может быть пустым");
        }

        if (containsVertex(vertex)) {
            throw new IllegalArgumentException(
                    "Вершина не существует: " + vertex);
        }
    }

    protected void checkVertexExists(String vertex) {
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException(
                    "Вершина не существует: " + vertex);
        }
    }

    protected int getVertexIndex(String vertex) {
        checkVertexExists(vertex);
        return vertices.indexOf(vertex);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Graph)) {
            return false;
        }

        Graph other = (Graph) object;

        List<String> ourVertices = getVertices();
        List<String> otherVertices = other.getVertices();

        if (ourVertices.size() != otherVertices.size()) {
            return false;
        }

        if (!ourVertices.containsAll(otherVertices)) {
            return false;
        }

        for (String vertex : ourVertices) {
            List<String> ourNeighbors = getNeighbors(vertex);
            List<String> otherNeighbors = other.getNeighbors(vertex);

            if (ourNeighbors.size() != otherNeighbors.size()) {
                return false;
            }

            if (!ourNeighbors.containsAll(otherNeighbors)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = new HashSet<>(getVertices()).hashCode();

        for (String vertex : getVertices()) {
            for (String neighbor : getNeighbors(vertex)) {
                result += 31 * vertex.hashCode() + neighbor.hashCode();
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Верщины: ").append(getVertices());
        result.append(", Рёбра: [");

        boolean firstEdge = true;

        for (String vertex : getVertices()) {
            for (String neighbor : getNeighbors(vertex)) {
                if (!firstEdge) {
                    result.append(", ");
                }

                result.append(vertex).append(" -> ").append(neighbor);
                firstEdge = false;
            }
        }

        result.append("]");

        return result.toString();
    }
}
