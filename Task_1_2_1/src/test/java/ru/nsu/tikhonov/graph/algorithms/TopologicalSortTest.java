package ru.nsu.tikhonov.graph.algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.graph.AdjacencyListGraph;
import ru.nsu.tikhonov.graph.AdjacencyMatrixGraph;
import ru.nsu.tikhonov.graph.Graph;
import ru.nsu.tikhonov.graph.IncidenceMatrixGraph;

/**
 * Тесты для алгоритмов топологической сортировки
 */
class TopologicalSortTest {

    private final List<TopologicalSort> sorts = List.of(
            new KahnTopologicalSort(),
            new DepthFirstTopologicalSort()
    );

    /**
     * Список является корректной топологической
     * сортировкой графа.
     */
    private void assertValidOrder(Graph graph, List<String> order) {
        assertEquals(graph.getVertices().size(), order.size());
        assertTrue(order.containsAll(graph.getVertices()));

        for (String from : graph.getVertices()) {
            for (String to : graph.getNeighbors(from)) {
                assertTrue(
                        order.indexOf(from) < order.indexOf(to),
                        "Ребро " + from + " -> " + to
                                + " нарушает порядок " + order
                );
            }
        }
    }

    /**
     * Корректная сортировка DAG обоими алгоритмами.
     */
    @Test
    void sortsDagCorrectly() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertValidOrder(graph, order);
            assertEquals("A", order.get(0));
            assertEquals("D", order.get(3));
        }
    }

    /**
     * Сортировка работает для графа без рёбер.
     */
    @Test
    void sortsGraphWithoutEdges() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertValidOrder(graph, order);
        }
    }

    /**
     * Сортировка пустого графа возвращает пустой список.
     */
    @Test
    void sortsEmptyGraph() {
        Graph graph = new AdjacencyListGraph();

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertTrue(order.isEmpty());
        }
    }

    /**
     * Сортировка графа из одной вершины возвращает
     * эту вершину.
     */
    @Test
    void sortsSingleVertex() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertEquals(List.of("A"), order);
        }
    }

    /**
     * При наличии цикла бросается исключение.
     */
    @Test
    void throwsOnCycle() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.addEdge("B", "A");

        for (TopologicalSort sort : sorts) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> sort.sort(graph)
            );
        }
    }

    /**
     * Петля трактуется как цикл.
     */
    @Test
    void throwsOnSelfLoop() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addEdge("A", "A");

        for (TopologicalSort sort : sorts) {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> sort.sort(graph)
            );
        }
    }

    /**
     * Сортировка на матрице смежности.
     */
    @Test
    void sortsAdjacencyMatrixGraph() {
        Graph graph = new AdjacencyMatrixGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertEquals(List.of("A", "B", "C"), order);
        }
    }

    /**
     * Сортировка на матрице инцидентности.
     */
    @Test
    void sortsIncidenceMatrixGraph() {
        Graph graph = new IncidenceMatrixGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");

        for (TopologicalSort sort : sorts) {
            List<String> order = sort.sort(graph);

            assertEquals(List.of("A", "B", "C"), order);
        }
    }

    /**
     * Алгоритмы Кана и DFS дают корректные
     * (возможно разные) результаты на одном и том же графе.
     */
    @Test
    void bothAlgorithmsProduceValidResults() {
        Graph graph = new AdjacencyListGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        List<String> kahn = new KahnTopologicalSort().sort(graph);
        List<String> dfs = new DepthFirstTopologicalSort().sort(graph);

        assertValidOrder(graph, kahn);
        assertValidOrder(graph, dfs);
    }
}