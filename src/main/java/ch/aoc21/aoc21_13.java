package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class aoc21_13 {
    public static void main(String[] args) {
        int[][] paper = load_paper("data/aoc21_13.txt");
        List<Split> splits = get_splits("data/aoc21_13.txt");

        int[][] folded_paper = fold_paper(paper, splits.get(0));

        Long counts1 = count_dots(folded_paper);
        System.out.println("Found " + counts1 + " dots after first fold.\n");

        for (int i = 1; i < splits.size(); ++i) {
            paper = folded_paper.clone();
            folded_paper = fold_paper(paper, splits.get(i));
        }

        print(folded_paper);
        System.out.println("done.");
    }

    private static void print(int[][] folded_paper) {
        System.out.println("\n\n");
        for (int[] ints : folded_paper) {
            StringBuilder line_string = new StringBuilder();
            for (int j = 0; j < folded_paper[0].length; ++j) {
                if (ints[j] > 0) {
                    line_string.append("#");
                } else {
                    line_string.append(".");
                }
            }
            System.out.println(line_string);
        }
        System.out.println("\n\n");
    }

    private static Long count_dots(int[][] folded_paper) {
        Long count = 0L;
        for (int[] ints : folded_paper) {
            for (int j = 0; j < folded_paper[0].length; ++j) {
                if (ints[j] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int[][] fold_paper(int[][] paper, Split split) {
        if (split.axis() == 'x') {
            return split_x_axis(paper, split.value());
        } else if (split.axis() == 'y') {
            return split_y_axis(paper, split.value());
        } else {
            throw new RuntimeException("unable to fold paper in axis " + split.axis());
        }
    }

    private static int[][] split_x_axis(int[][] paper, int value) {
        int[][] new_paper = new int[paper.length][value];

        for (int x = 0; x < value; ++x) {
            for (int j = 0; j < paper.length; ++j) {
                new_paper[j][x] = paper[j][x];
            }
        }

        int i = 1;
        for (int x = value + 1; x < paper[0].length; ++x) {
            for (int j = 0; j < paper.length; ++j) {
                new_paper[j][value - i] += paper[j][x];
            }
            i++;
        }

        return new_paper;
    }

    private static int[][] split_y_axis(int[][] paper, int value) {
        int[][] new_paper = new int[value][paper[0].length];

        for (int y = 0; y < value; ++y) {
            System.arraycopy(paper[y], 0, new_paper[y], 0, paper[0].length);
        }

        int i = 1;
        for (int y = value + 1; y < paper.length; ++y) {
            for (int j = 0; j < paper[0].length; ++j) {
                new_paper[value - i][j] += paper[y][j];
            }
            i++;
        }

        return new_paper;

    }

    private static List<Split> get_splits(String data_path) {
        List<Split> splits = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            boolean init = false;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    init = true;
                } else {
                    if (init) {
                        String[] split = line.split(" ");
                        Split new_split = new Split(split[2].split("=")[0].charAt(0), Integer.parseInt(split[2].split("=")[1]));
                        splits.add(new_split);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return splits;
    }

    private static int[][] load_paper(String data_path) {
        List<Integer> x_coords = new ArrayList<>();
        List<Integer> y_coords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String[] split = line.split(",");
                    x_coords.add(Integer.parseInt(split[0]));
                    y_coords.add(Integer.parseInt(split[1]));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int x_max = Collections.max(x_coords) + 1;
        int y_max = Collections.max(y_coords) + 1;

        int[][] field = new int[y_max][x_max];

        fill_arr(field, 0);
        for (int i = 0; i < x_coords.size(); ++i) {
            field[y_coords.get(i)][x_coords.get(i)] = 1;
        }
        return field;
    }

    private static void fill_arr(int[][] field, int number) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = number;
            }
        }
    }
}

record Split(Character axis, int value) {
}
