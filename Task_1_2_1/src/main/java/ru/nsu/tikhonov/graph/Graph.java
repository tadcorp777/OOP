package ru.nsu.tikhonov.graph;

import java.io.IOException;
import java.util.List;

public interface Graph {

    void addVertex(String vertex);

    void removeVertex(String vertex);

    void addEdge(String from, String to);

    void removeEdge(String from, String to);

    List<String> getNeighbors(String vertex);

    List<String> getVertices();

    boolean containsVertex(String vertex);

    boolean containsEdge(String from, String to);

    void readFromFile(String fileName) throws IOException;
}
