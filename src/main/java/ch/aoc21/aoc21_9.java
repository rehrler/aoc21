package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class aoc21_9 {

    public static void main(String[] args) {

        int[][] field = load_field("data/aoc21_9.txt");

        Long sum_risk_levels = filter_field(field, 1);

        System.out.println("Part1 sum of risk levels: " + sum_risk_levels + ". \n");

        int[][] seg_field = segment_field(field);

        Long sol_2 = calc_sol2(field, seg_field, 1);

        System.out.println("Part2 product of the greatest basins: " + sol_2 + ". \n");

        System.out.println("done.");
    }

    private static Long calc_sol2(int[][] field, int[][] seg_field, int size) {
        List<Long> basin_sizes = new ArrayList<>();
        for (int i_idx = 0; i_idx < field.length; ++i_idx) {
            for (int j_idx = 0; j_idx < field[0].length; ++j_idx) {
                boolean low_point = true;
                for (int shift = -size; shift <= size; ++shift) {
                    if (shift != 0) {
                        if (i_idx + shift >= 0 && i_idx + shift < field.length) {
                            if (field[i_idx + shift][j_idx] <= field[i_idx][j_idx]) {
                                low_point = false;
                                break;
                            }
                        }
                        if (j_idx + shift >= 0 && j_idx + shift < field[0].length) {
                            if (field[i_idx][j_idx + shift] <= field[i_idx][j_idx]) {
                                low_point = false;
                                break;
                            }
                        }
                    }
                }
                if (low_point) {
                    Long basin_size = calc_neighbours(i_idx, j_idx, seg_field, size);
                    basin_sizes.add(basin_size);
                }
            }
        }
        basin_sizes.sort(null);
        Collections.reverse(basin_sizes);
        return basin_sizes.get(0) * basin_sizes.get(1) * basin_sizes.get(2);

    }

    private static Long calc_neighbours(int i_idx, int j_idx, int[][] seg_field, int size) {
        Long total_size = 0L;

        int[][] star_field = seg_field.clone();
        star_field[i_idx][j_idx] = -1;
        total_size++;

        for (int shift = -size; shift <= size; ++shift) {
            if (shift != 0) {
                if (i_idx + shift >= 0 && i_idx + shift < seg_field.length) {
                    if (star_field[i_idx + shift][j_idx] != -1 && star_field[i_idx + shift][j_idx] != 1) {
                        total_size += calc_neighbours(i_idx + shift, j_idx, star_field, 1);
                    }
                }
                if (j_idx + shift >= 0 && j_idx + shift < seg_field[0].length) {
                    if (star_field[i_idx][j_idx + shift] != -1 && star_field[i_idx][j_idx + shift] != 1) {
                        total_size += calc_neighbours(i_idx, j_idx + shift, star_field, 1);
                    }
                }
            }
        }

        return total_size;
    }

    private static int[][] segment_field(int[][] field) {
        int[][] seg_field = new int[field.length][field[0].length];
        for (int i_idx = 0; i_idx < field.length; ++i_idx) {
            for (int j_idx = 0; j_idx < field[0].length; ++j_idx) {
                if (field[i_idx][j_idx] < 9) {
                    seg_field[i_idx][j_idx] = 0;
                } else {
                    seg_field[i_idx][j_idx] = 1;
                }
            }
        }
        return seg_field;
    }

    private static Long filter_field(int[][] field, int size) {
        long summed_risk_values = 0L;
        for (int i_idx = 0; i_idx < field.length; ++i_idx) {
            for (int j_idx = 0; j_idx < field[0].length; ++j_idx) {
                boolean low_point = true;
                for (int shift = -size; shift <= size; ++shift) {
                    if (shift != 0) {
                        if (i_idx + shift >= 0 && i_idx + shift < field.length) {
                            if (field[i_idx + shift][j_idx] <= field[i_idx][j_idx]) {
                                low_point = false;
                                break;
                            }
                        }
                        if (j_idx + shift >= 0 && j_idx + shift < field[0].length) {
                            if (field[i_idx][j_idx + shift] <= field[i_idx][j_idx]) {
                                low_point = false;
                                break;
                            }
                        }
                    }
                }
                if (low_point) {
                    summed_risk_values += field[i_idx][j_idx] + 1;
                }
            }
        }
        return summed_risk_values;
    }

    private static int[][] load_field(String data_path) {
        int[][] field = new int[100][100];
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int i_idx = 0;
            while ((line = br.readLine()) != null) {
                int j_idx = 0;
                for (Character new_char : line.toCharArray()) {
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
