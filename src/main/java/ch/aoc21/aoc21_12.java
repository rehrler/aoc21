package ch.aoc21;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Character.isUpperCase;

public class aoc21_12 {

    public static void main(String[] args) {
        Graph graph = load_graph("data/aoc21_12.txt");

        // part 1
        Set<String> paths = new HashSet<>();
        Set<String> small_nodes_visited = new HashSet<>();
        List<String> start = new ArrayList<>();
        start.add("start");
        evaluate_poss_paths(start, graph.getGraph(), small_nodes_visited, paths, false);
        System.out.println("Part1; Found " + paths.size() + " in given graph.\n");

        // part 2
        paths = new HashSet<>();
        small_nodes_visited = new HashSet<>();
        start = new ArrayList<>();
        start.add("start");
        evaluate_poss_paths(start, graph.getGraph(), small_nodes_visited, paths, true);
        System.out.println("Part2; Found " + paths.size() + " in given graph.\n");

        System.out.println("done");
    }

    private static void evaluate_poss_paths(List<String> current_path, Map<String, Node> graph, Set<String> small_nodes_visited, Set<String> paths, boolean can_visit_again) {

        String current = current_path.get(current_path.size() - 1);

        if (current.equals("end")) {
            paths.add(String.join(",", current_path));
            return;
        }
        small_nodes_visited = new HashSet<>(small_nodes_visited);
        for (Character curr_char : current.toCharArray()) {
            if (!isUpperCase(curr_char)) {
                small_nodes_visited.add(current);
                break;
            }
        }

        Set<String> connections = graph.get(current).getConnections();
        for (String connection : connections) {
            List<String> next_path = new ArrayList<>(current_path);
            next_path.add(connection);
            if (!can_visit_again) {
                if (!small_nodes_visited.contains(connection)) {
                    evaluate_poss_paths(next_path, graph, small_nodes_visited, paths, false);
                }
            } else if (!connection.equals("start")) {
                evaluate_poss_paths(next_path, graph, small_nodes_visited, paths, !small_nodes_visited.contains(connection));
            }
        }

    }

    private static Graph load_graph(String data_path) {
        Graph graph = new Graph();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("-");
                graph.add_nodes(split[0], split[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }
}

class Graph {
    Map<String, Node> graph = new HashMap<>();

    public Map<String, Node> getGraph() {
        return graph;
    }

    public void add_nodes(String node1, String node2) {
        if (graph.containsKey(node1)) {
            graph.get(node1).add_connection(node2);
        } else {
            graph.put(node1, new Node(node1, node2));
        }

        if (graph.containsKey(node2)) {
            graph.get(node2).add_connection(node1);
        } else {
            graph.put(node2, new Node(node2, node1));
        }
    }

}

class Node {
    private final String name;
    private final Set<String> connections = new HashSet<>();

    public Node(String node, String conn) {
        name = node;
        connections.add(conn);
    }

    public void add_connection(String conn) {
        connections.add(conn);
    }

    public Set<String> getConnections() {
        return connections;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return this.name.equals(((Node) o).name);
        }
        return false;
    }


}