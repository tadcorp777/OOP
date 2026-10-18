package ru.nsu.tikhonov.graph.algorithms;

import ru.nsu.tikhonov.graph.Graph;
import java.util.List;

public interface TopologicalSort {

    List<String> sort(Graph graph);
}
