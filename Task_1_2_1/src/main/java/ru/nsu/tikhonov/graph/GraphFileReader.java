
package ru.nsu.tikhonov.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GraphFileReader {

    public static void readFromFile(String fileName, Graph graph)
            throws IOException {

        boolean edgesStarted = false;

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts[0].equals("V")) {
                    if (edgesStarted) {
                        throw new IOException(
                                "Вершины должны быть указаны перед ребрами. Строка: "
                                        + lineNumber);
                    }

                    if (parts.length != 2) {
                        throw new IOException(
                                "Недопустимый формат вершин в строке: "
                                        + lineNumber);
                    }

                    graph.addVertex(parts[1]);

                } else if (parts[0].equals("E")) {
                    if (parts.length != 3) {
                        throw new IOException(
                                "Недопустимый формат ребра в строке: "
                                        + lineNumber);
                    }

                    if (!graph.containsVertex(parts[1])
                            || !graph.containsVertex(parts[2])) {
                        throw new IOException(
                                "Ребро ссылается на несуществующую "
                                        + "вершину в строке: "
                                        + lineNumber);
                    }

                    edgesStarted = true;
                    graph.addEdge(parts[1], parts[2]);

                } else {
                    throw new IOException(
                            "Неизвестная команда в строке: " + lineNumber);
                }
            }
        }
    }
}
