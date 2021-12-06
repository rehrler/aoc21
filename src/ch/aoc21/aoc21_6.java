package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class aoc21_6 {
    public static void main(String[] args) {
        int days = 256;
        Long[] fishes = load_fishes("data/aoc21_6.txt");
        Long nb_fishes = sim_fishes(days, fishes);

        System.out.println("Nb of fishes after " + days + " days: " + nb_fishes);

        System.out.println("done.");
    }

    private static Long sim_fishes(int days, Long[] fishes) {
        for (int i = 0; i < days; ++i) {
            Long[] new_fishes = shift_array(fishes);

            new_fishes[8] += fishes[0];
            fishes = new_fishes;
        }

        return Arrays.stream(fishes).mapToLong(Long::longValue).sum();
    }

    private static Long[] shift_array(Long[] fishes) {
        Long[] new_fishes = new Long[9];
        Arrays.fill(new_fishes, 0L);
        new_fishes[6] = fishes[0];
        for (int i = 1; i < 9; ++i) {
            new_fishes[i - 1] += fishes[i];
        }
        return new_fishes;
    }

    private static Long[] load_fishes(String data_path) {
        Long[] fishes = new Long[9];
        Arrays.fill(fishes, 0L);
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                for (String str_nb : split) {
                    fishes[Integer.parseInt(str_nb)]++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fishes;
    }
}