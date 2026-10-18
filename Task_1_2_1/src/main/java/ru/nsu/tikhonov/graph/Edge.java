package ru.nsu.tikhonov.graph;

public class Edge {

    private final String from;
    private final String to;

    public Edge(String from, String to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Имя вершины не может быть пустым");
        }

        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Edge)) {
            return false;
        }

        Edge other = (Edge) object;

        return from.equals(other.from) && to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return 31 * from.hashCode() + to.hashCode();
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }
}
