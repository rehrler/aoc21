package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.math.IntMath.gcd;
import static java.lang.Math.abs;


public class aoc21_17 {
    public static void main(String[] args) {
        Set<Coordinate> coordinates = parse_input("data/aoc21_17.txt");

        int max_height = calc_max_height(coordinates);
        System.out.println("Part1; max. height: " + max_height + ".\n");

        int dist_vel_inputs = calc_dist_vel_inputs(coordinates);
        System.out.println("Part2; distinct velocity inputs: " + dist_vel_inputs + ".\n");

        System.out.println("done.");
    }

    private static int calc_dist_vel_inputs(Set<Coordinate> coordinates) {
        Set<Coordinate> poss_input_vel = new HashSet<>();
        for (int v_x = 0; v_x < 300; ++v_x) {
            for (int v_y = -300; v_y < 300; ++v_y) {
                Coordinate point = new Coordinate(0, 0);
                Coordinate vel = new Coordinate(v_x, v_y);
                for (int n = 0; n < 500; ++n) {
                    if (coordinates.contains(point)) {
                        poss_input_vel.add(new Coordinate(v_x, v_y));
                        break;
                    }
                    point = new Coordinate(point.x() + vel.x(), point.y() + vel.y());
                    vel = new Coordinate(vel.x() > 0 ? vel.x() - 1 : (vel.x() < 0 ? vel.x() + 1 : 0), vel.y() - 1);
                }
            }
        }
        return poss_input_vel.size();
    }


    private static int calc_max_height(Set<Coordinate> coordinates) {
        int y_max = 0;
        for (Coordinate coord : coordinates) {
            int gcd = gcd(coord.x(), abs(coord.y()));
            if (gcd % 2 == 1) {
                int v_y = coord.y() / gcd + (gcd - 1) / 2;
                int poss_imax2 = v_y + 1;
                if (v_y < gcd) {
                    if (v_y * v_y - (v_y * (v_y - 1)) / 2 > y_max) {
                        y_max = v_y * v_y - (v_y * (v_y - 1)) / 2;
                    }
                }
                if (poss_imax2 < gcd) {
                    if (poss_imax2 * v_y - (poss_imax2 * (poss_imax2 - 1)) / 2 > y_max) {
                        y_max = poss_imax2 * v_y - (poss_imax2 * (poss_imax2 - 1)) / 2;
                    }
                }
            }
        }
        return y_max;
    }

    private static Set<Coordinate> parse_input(String data_path) {
        StringBuilder total_str = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                total_str.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] first_split = total_str.toString().split("\\: ");
        String[] second_split = first_split[1].split("\\, ");
        String[] x_split = second_split[0].split("\\=")[1].split("\\.\\.");
        String[] y_split = second_split[1].split("\\=")[1].split("\\.\\.");
        int xmin = Integer.parseInt(x_split[0]);
        int xmax = Integer.parseInt(x_split[1]);
        int ymin = Integer.parseInt(y_split[0]);
        int ymax = Integer.parseInt(y_split[1]);
        Set<Coordinate> coordinates = new HashSet<>();
        for (int x = xmin; x <= xmax; ++x) {
            for (int y = ymin; y <= ymax; ++y) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        return coordinates;
    }
}

record Coordinate(int x, int y) {
}