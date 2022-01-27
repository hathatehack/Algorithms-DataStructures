package C11_Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class C01_Graph {
    public static class Node<T> {
        public T value;
        public int in;
        public int out;
        public LinkedList<Node<T>> toNodes;
        public LinkedList<Edge> edges;

        public Node(T _value) {
            value = _value;
            in = 0;
            out = 0;
            toNodes = new LinkedList<>();
            edges = new LinkedList<>();
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    public static class Edge {
        public Node from;
        public Node to;
        public int weight;

        public Edge(Node _from, Node _to, int _weight) {
            from = _from;
            to = _to;
            weight = _weight;
        }

        public Edge(Node _from, Node _to) {
            from = _from;
            to = _to;
            weight = 0;
        }
    }

    public static class Graph<T> {
        public HashMap<T, Node<T>> nodes;
        public HashSet<Edge> edges;

        public Graph() {
            nodes = new HashMap<>();
            edges = new HashSet<>();
        }
    }

    // 将多组[from, to, weight]信息转化为graph
    public static Graph<Integer> createGraph(int[][] matrix) {
        Graph<Integer> graph = new Graph<>();
        for (int[] array : matrix) {
            int from = array[0];
            int to = array[1];
            int w = array[2];
            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from, new Node<>(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node<>(to));
            }
            Node<Integer> fromNode = graph.nodes.get(from);
            Node<Integer> toNode = graph.nodes.get(to);
            Edge edge = new Edge(fromNode, toNode, w);
            toNode.in++;
            fromNode.out++;
            fromNode.toNodes.add(toNode);
            fromNode.edges.add(edge);
            graph.edges.add(edge);
        }
        return graph;
    }

    // 将多组[from, to]信息转化为graph
    public static Graph<String> createGraph(String[][] matrix) {
        Graph<String> graph = new Graph<>();
        for (String[] array : matrix) {
            String from = array[0];
            String to = array[1];
            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from, new Node<>(from));
            }
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node<>(to));
            }
            Node<String> fromNode = graph.nodes.get(from);
            Node<String> toNode = graph.nodes.get(to);
            Edge edge = new Edge(fromNode, toNode);
            toNode.in++;
            fromNode.out++;
            fromNode.toNodes.add(toNode);
            fromNode.edges.add(edge);
            graph.edges.add(edge);
        }
        return graph;
    }
}
