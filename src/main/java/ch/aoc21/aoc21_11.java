package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class aoc21_11 {

    public static void main(String[] args) {
        String data_path = "data/aoc21_11.txt";

        OctopusSim octopusSim = new OctopusSim(load_field(data_path));
        Long flashes = octopusSim.sim(100);
        System.out.println("Part1; found " + flashes + " in 100 sim steps.\n");

        octopusSim = new OctopusSim(load_field(data_path));
        int sim_step = octopusSim.get_stop_sim();
        System.out.println("Part2; found all flashes at sim step: " + sim_step + ".\n");

        System.out.println("done.");
    }


    private static int[][] load_field(String data_path) {
        int[][] field = new int[10][10];
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int i_idx = 0;
            while ((line = br.readLine()) != null) {
                int j_idx = 0;
                for (char new_char : line.toCharArray()) {
                    field[i_idx][j_idx] = Integer.parseInt(String.valueOf(new_char));
                    j_idx++;
                }
                i_idx++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return field;
    }
}

record Index(int i, int j) {
}

class OctopusSim {
    private static int row_dim, col_dim;
    private final int[][] octopus_field;

    public OctopusSim(int[][] oct_field) {
        octopus_field = oct_field;
        row_dim = oct_field.length;
        col_dim = oct_field[0].length;
    }

    public Long sim(int duration) {
        Long total_flashes = 0L;
        while (duration != 0) {
            increase_field(1);
            total_flashes += check_flashes();

            duration--;
        }

        return total_flashes;
    }

    private Long check_flashes() {
        Set<Index> visited_idx = new HashSet<>();
        int new_size = 0;
        int old_size = -1;
        while (new_size != old_size) {
            old_size = new_size;
            for (int i_idx = 0; i_idx < row_dim; ++i_idx) {
                for (int j_idx = 0; j_idx < col_dim; ++j_idx) {
                    if (octopus_field[i_idx][j_idx] == 0 && !visited_idx.contains(new Index(i_idx, j_idx))) {
                        increase_neighbours(i_idx, j_idx, 1);
                        visited_idx.add(new Index(i_idx, j_idx));
                    }
                }
            }
            new_size = visited_idx.size();
        }

        return count_zeros();
    }

    private Long count_zeros() {
        Long flashes = 0L;
        for (int i_idx = 0; i_idx < row_dim; ++i_idx) {
            for (int j_idx = 0; j_idx < col_dim; ++j_idx) {
                if (octopus_field[i_idx][j_idx] == 0) {
                    flashes++;
                }
            }
        }
        return flashes;
    }

    private void increase_neighbours(int i_idx, int j_idx, int add) {
        for (int ii_idx = i_idx - 1; ii_idx < i_idx + 2; ++ii_idx) {
            for (int jj_idx = j_idx - 1; jj_idx < j_idx + 2; ++jj_idx) {
                if (ii_idx >= 0 && ii_idx < row_dim && jj_idx >= 0 && jj_idx < col_dim) {
                    if (octopus_field[ii_idx][jj_idx] < 9 && octopus_field[ii_idx][jj_idx] != 0) {
                        octopus_field[ii_idx][jj_idx] += add;
                    } else {
                        octopus_field[ii_idx][jj_idx] = 0;
                    }
                }
            }
        }
    }

    private void increase_field(int add) {
        for (int i_idx = 0; i_idx < row_dim; ++i_idx) {
            for (int j_idx = 0; j_idx < col_dim; ++j_idx) {
                if (octopus_field[i_idx][j_idx] < 9) {
                    octopus_field[i_idx][j_idx] += add;
                } else {
                    octopus_field[i_idx][j_idx] = 0;
                }
            }
        }
    }

    public int get_stop_sim() {
        int sim_step=1;
        while (true) {
            increase_field(1);
            Long nb_flashes = check_flashes();
            if (nb_flashes==100L) {
                break;
            }
            sim_step++;
        }
        return sim_step;
    }
}