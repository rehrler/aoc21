package ch.aoc21;

import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class aoc21_20 {
    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int[][] image = parse_img("data/aoc21_20.txt", 100);
        List<Integer> algo = parse_algo("data/aoc21_20.txt");
        int i = 0;
        while (i < 2) {
            image = enhance_img(image, algo, i);
            i++;
        }

        System.out.println("Part1; found " + count_ones(image) + " white pixels.\n");

        while (i < 50) {
            image = enhance_img(image, algo, i);
            i++;
        }

        System.out.println("Part2; found " + count_ones(image) + " white pixels.\n");

        stopwatch.stop();
        System.out.println("Time elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.\n");
        System.out.println("done.");
    }

    private static Long count_ones(int[][] image) {
        Long white_px = 0L;
        for (int[] ints : image) {
            for (int j = 0; j < image[0].length; ++j) {
                if (ints[j] == 1) {
                    white_px++;
                }
            }
        }
        return white_px;
    }

    private static int[][] enhance_img(int[][] image, List<Integer> algo, int repetition) {
        int[][] new_img = new int[image.length + 2][image[0].length + 2];

        if (repetition % 2 == 0) {
            fill_arr(new_img, algo.get(0));
        } else {
            fill_arr(new_img, algo.get(algo.size() - 1));
        }

        for (int i = 1; i < image.length - 1; ++i) {
            for (int j = 1; j < image[0].length - 1; ++j) {
                int code = calc_code(i, j, image);
                new_img[i + 1][j + 1] = algo.get(code);
            }
        }

        return new_img;
    }

    private static int calc_code(int i, int j, int[][] image) {
        StringBuilder byte_code = new StringBuilder();
        for (int ii = i - 1; ii < i + 2; ++ii) {
            for (int jj = j - 1; jj < j + 2; ++jj) {
                byte_code.append(image[ii][jj]);
            }
        }
        return Integer.parseInt(byte_code.toString(), 2);
    }

    private static int[][] parse_img(String data_path, int size) {
        int[][] img = new int[size + 4][size + 4];
        fill_arr(img, 0);
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int line_nb = 0;
            int i_idx = 2;
            while ((line = br.readLine()) != null) {
                if (line_nb > 1) {
                    int j_idx = 2;
                    for (Character character : line.toCharArray()) {
                        if (character == '.') {
                            img[i_idx][j_idx] = 0;
                        } else if (character == '#') {
                            img[i_idx][j_idx] = 1;
                        } else {
                            throw new RuntimeException("wrong sign in algorithm sequence");
                        }
                        j_idx++;
                    }
                    i_idx++;
                }
                line_nb++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private static List<Integer> parse_algo(String data_path) {
        List<Integer> algo = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int line_nb = 0;
            while ((line = br.readLine()) != null) {
                if (line_nb < 1) {
                    for (Character character : line.toCharArray()) {
                        if (character == '.') {
                            algo.add(0);
                        } else if (character == '#') {
                            algo.add(1);
                        } else {
                            throw new RuntimeException("wrong sign in algorithm sequence");
                        }
                    }
                }

                line_nb++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return algo;
    }

    private static void fill_arr(int[][] arr, int value) {
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[0].length; ++j) {
                arr[i][j] = value;
            }
        }
    }
}
