package ru.nsu.tikhonov.graph;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Тесты для реализации графа на матрице инцидентности
 */
class IncidenceMatrixGraphTest {

    private final Graph graph = new IncidenceMatrixGraph();

    /**
     * Добавление вершины.
     */
    @Test
    void addVertexAddsVertex() {
        graph.addVertex("A");

        assertTrue(graph.containsVertex("A"));
        assertEquals(List.of("A"), graph.getVertices());
    }

    /**
     * Повторное добавление вершины бросает исключение.
     */
    @Test
    void addVertexThrowsOnDuplicate() {
        graph.addVertex("A");

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addVertex("A")
        );
    }

    /**
     * null-вершина недопустима.
     */
    @Test
    void addVertexThrowsOnNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addVertex(null)
        );
    }

    /**
     * Пустая вершина недопустима.
     */
    @Test
    void addVertexThrowsOnBlank() {
        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addVertex(" ")
        );
    }

    /**
     * Удаление существующей вершины.
     */
    @Test
    void removeVertexRemovesVertex() {
        graph.addVertex("A");
        graph.addVertex("B");

        graph.removeVertex("A");

        assertFalse(graph.containsVertex("A"));
        assertEquals(List.of("B"), graph.getVertices());
    }

    /**
     * Удаление несуществующей вершины бросает исключение.
     */
    @Test
    void removeVertexThrowsWhenMissing() {
        assertThrows(
                IllegalArgumentException.class,
                () -> graph.removeVertex("A")
        );
    }

    /**
     * Удаление вершины и всех связанных рёбер.
     */
    @Test
    void removeVertexRemovesRelatedEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");

        graph.removeVertex("B");

        assertTrue(graph.containsEdge("A", "C"));
        assertFalse(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "C"));
        assertEquals(List.of("C"), graph.getNeighbors("A"));
    }

    /**
     * Добавление ребра.
     */
    @Test
    void addEdgeAddsEdge() {
        graph.addVertex("A");
        graph.addVertex("B");

        graph.addEdge("A", "B");

        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"));
    }

    /**
     * Повторное добавление ребра игнорируется.
     */
    @Test
    void addEdgeDuplicateIsIgnored() {
        graph.addVertex("A");
        graph.addVertex("B");

        graph.addEdge("A", "B");
        graph.addEdge("A", "B");

        assertEquals(1, graph.getNeighbors("A").size());
    }

    /**
     * Ребро нельзя добавить между несуществующими
     * вершинами.
     */
    @Test
    void addEdgeThrowsWhenVerticesMissing() {
        graph.addVertex("A");

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addEdge("A", "B")
        );
    }

    /**
     * Удаление ребра.
     */
    @Test
    void removeEdgeRemovesEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        graph.removeEdge("A", "B");

        assertFalse(graph.containsEdge("A", "B"));
    }

    /**
     * Удаление несуществующего ребра (без исключения).
     */
    @Test
    void removeEdgeNonExistingDoesNothing() {
        graph.addVertex("A");
        graph.addVertex("B");

        assertDoesNotThrow(() -> graph.removeEdge("A", "B"));
    }

    /**
     * Удаление ребра между несуществующими вершинами
     * бросает исключение.
     */
    @Test
    void removeEdgeThrowsWhenVerticesMissing() {
        assertThrows(
                IllegalArgumentException.class,
                () -> graph.removeEdge("A", "B")
        );
    }

    /**
     * Получение соседей вершины.
     */
    @Test
    void getNeighborsReturnsNeighbors() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");

        List<String> neighbors = graph.getNeighbors("A");

        assertEquals(2, neighbors.size());
        assertTrue(neighbors.containsAll(List.of("B", "C")));
    }

    /**
     * Получение соседей несуществующей вершины бросает
     * исключение.
     */
    @Test
    void getNeighborsThrowsWhenMissing() {
        assertThrows(
                IllegalArgumentException.class,
                () -> graph.getNeighbors("A")
        );
    }

    /**
     * Graph.getVertices возвращает копию.
     */
    @Test
    void getVerticesReturnsCopy() {
        graph.addVertex("A");

        List<String> vertices = graph.getVertices();
        vertices.add("B");

        assertFalse(graph.containsVertex("B"));
    }

    /**
     * Graph.containsEdge для отсутствующих вершин.
     */
    @Test
    void containsEdgeReturnsFalseWhenVerticesMissing() {
        assertFalse(graph.containsEdge("A", "B"));
    }

    /**
     * ПоддержкаП петли.
     */
    @Test
    void selfLoopIsSupported() {
        graph.addVertex("A");

        graph.addEdge("A", "A");

        assertTrue(graph.containsEdge("A", "A"));
        assertEquals(List.of("A"), graph.getNeighbors("A"));
    }

    /**
     * Петля не создаётся повторно.
     */
    @Test
    void selfLoopDuplicateIsIgnored() {
        graph.addVertex("A");

        graph.addEdge("A", "A");
        graph.addEdge("A", "A");

        assertEquals(1, graph.getNeighbors("A").size());
    }

    /**
     * Равенство и hashCode одинаковых графов.
     */
    @Test
    void equalsAndHashCodeForEqualGraphs() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        Graph other = new IncidenceMatrixGraph();
        other.addVertex("A");
        other.addVertex("B");
        other.addEdge("A", "B");

        assertEquals(graph, other);
        assertEquals(graph.hashCode(), other.hashCode());
    }

    /**
     * Графы с разными рёбрами не равны.
     */
    @Test
    void equalsReturnsFalseForDifferentGraphs() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        Graph other = new IncidenceMatrixGraph();
        other.addVertex("A");
        other.addVertex("B");
        other.addEdge("B", "A");

        assertNotEquals(graph, other);
    }

    /**
     * Равенство с другой реализацией графа.
     */
    @Test
    void equalsAcrossDifferentImplementations() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        Graph list = new AdjacencyListGraph();
        list.addVertex("A");
        list.addVertex("B");
        list.addEdge("A", "B");

        assertEquals(graph, list);
        assertEquals(list, graph);
        assertEquals(graph.hashCode(), list.hashCode());
    }

    /**
     * Граф не равен null и объекту другого типа.
     */
    @Test
    void equalsReturnsFalseForNullOrOtherType() {
        graph.addVertex("A");

        assertNotEquals(graph, null);
        assertNotEquals(graph, new Object());
    }

    /**
     * Строковое представление графа.
     */
    @Test
    void toStringContainsVerticesAndEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");

        String result = graph.toString();

        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("->"));
    }

    /**
     * Чтение графа из файла.
     */
    @Test
    void readFromFileReadsGraph(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("graph.txt");
        Files.write(file, List.of(
                "V A",
                "V B",
                "V C",
                "E A B",
                "E B C"
        ));

        graph.readFromFile(file.toString());

        assertEquals(3, graph.getVertices().size());
        assertTrue(graph.containsEdge("A", "B"));
        assertTrue(graph.containsEdge("B", "C"));
        assertFalse(graph.containsEdge("A", "C"));
    }

    /**
     * Неверный формат строки ребра.
     */
    @Test
    void readFromFileThrowsOnInvalidEdgeFormat(@TempDir Path tempDir)
            throws IOException {
        Path file = tempDir.resolve("bad.txt");
        Files.write(file, List.of(
                "V A",
                "V B",
                "E A"
        ));

        assertThrows(
                IOException.class,
                () -> graph.readFromFile(file.toString())
        );
    }
}