package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class aoc21_15 {

    public static void main(String[] args) {
        int[][] pattern = load_pattern("data/aoc21_15.txt", 100);
        Long cost_sp = calc_shortest_path(pattern);
        System.out.println("Part1; cost for shortest path: " + cost_sp + ".\n");

        int[][] pattern2 = create_pattern2(pattern);
        Long cost_sp2 = calc_shortest_path(pattern2);
        System.out.println("Part2; cost for shortest path: " + cost_sp2 + ".\n");
        System.out.println("done.");
    }

    private static int[][] create_pattern2(int[][] pattern) {
        int[][] pattern2 = new int[pattern.length * 5][pattern[0].length * 5];
        int i = 0;
        int i_inc = 0;
        while (i != pattern.length * 5) {
            int[][] first_pattern = create_first_pattern(pattern, i_inc);
            for (int ii = 0; ii < pattern.length; ++ii) {
                System.arraycopy(first_pattern[ii], 0, pattern2[i + ii], 0, pattern[0].length);
            }
            int j = pattern[0].length;
            int j_inc = 1;

            while (j != pattern[0].length * 5) {
                int[][] second_pattern = create_first_pattern(first_pattern, j_inc);
                for (int ii = 0; ii < pattern.length; ++ii) {
                    System.arraycopy(second_pattern[ii], 0, pattern2[i + ii], j, pattern[0].length);
                }
                j += pattern[0].length;
                j_inc++;
            }
            i += pattern.length;
            i_inc++;
        }
        return pattern2;
    }

    private static int[][] create_first_pattern(int[][] pattern, int add) {
        int[][] new_pattern = new int[pattern.length][pattern[0].length];
        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern.length; ++j) {
                if ((pattern[i][j] + add) % 9 == 0) {
                    new_pattern[i][j] = 9;
                } else {
                    new_pattern[i][j] = (pattern[i][j] + add) % 9;
                }
            }
        }
        return new_pattern;
    }

    private static Long calc_shortest_path(int[][] pattern) {
        Set<NodeCoord> OPEN = new HashSet<>();
        Map<NodeCoord, Long> costs = new HashMap<>();
        NodeCoord start_node = new NodeCoord(0, 0);
        NodeCoord term_node = new NodeCoord(pattern.length - 1, pattern[0].length - 1);
        OPEN.add(start_node);
        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[0].length; ++j) {
                costs.put(new NodeCoord(i, j), 100000000L);
            }
        }
        costs.put(start_node, 0L);

        while (!OPEN.isEmpty()) {


            NodeCoord removed_node = dijkstra_remove(OPEN, costs);

            Set<NodeCoord> children_nodes = get_children_nodes(removed_node, pattern);
            for (NodeCoord child : children_nodes) {
                if (((costs.get(removed_node) + pattern[child.i()][child.j()]) < costs.get(child)) && ((costs.get(removed_node) + pattern[child.i()][child.j()]) < costs.get(term_node))) {
                    costs.put(child, costs.get(removed_node) + pattern[child.i()][child.j()]);
                    if (!(child.i() == term_node.i() && child.j() == term_node.j())) {
                        OPEN.add(child);
                    }
                }
            }
        }
        return costs.get(term_node);
    }

    private static NodeCoord dijkstra_remove(Set<NodeCoord> open, Map<NodeCoord, Long> costs) {
        Long lowest_cost = Long.MAX_VALUE;
        NodeCoord lowest_cost_node = null;
        for (NodeCoord coord : open) {
            if (lowest_cost > costs.get(coord)) {
                lowest_cost_node = coord;
                lowest_cost = costs.get(coord);
            }
        }
        open.remove(lowest_cost_node);
        return lowest_cost_node;
    }


    private static Set<NodeCoord> get_children_nodes(NodeCoord removed_node, int[][] pattern) {
        Set<NodeCoord> children = new HashSet<>();
        NodeCoord left_node = new NodeCoord(removed_node.i(), removed_node.j() - 1);
        NodeCoord right_node = new NodeCoord(removed_node.i(), removed_node.j() + 1);
        NodeCoord up_node = new NodeCoord(removed_node.i() - 1, removed_node.j());
        NodeCoord down_node = new NodeCoord(removed_node.i() + 1, removed_node.j());
        if (check_node(left_node, pattern)) {
            children.add(left_node);
        }
        if (check_node(right_node, pattern)) {
            children.add(right_node);
        }
        if (check_node(up_node, pattern)) {
            children.add(up_node);
        }
        if (check_node(down_node, pattern)) {
            children.add(down_node);
        }
        return children;
    }

    private static boolean check_node(NodeCoord node_to_check, int[][] pattern) {
        return node_to_check.i() >= 0 && node_to_check.i() < pattern.length && node_to_check.j() >= 0 && node_to_check.j() < pattern[0].length;
    }

    private static int[][] load_pattern(String data_path, int size) {
        int[][] new_map = new int[size][size];
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int i_idx = 0;
            while ((line = br.readLine()) != null) {
                int j_idx = 0;
                for (Character character : line.toCharArray()) {
                    new_map[i_idx][j_idx] = Integer.parseInt(String.valueOf(character));
                    j_idx++;
                }
                i_idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new_map;
    }
}

record NodeCoord(int i, int j) {

}