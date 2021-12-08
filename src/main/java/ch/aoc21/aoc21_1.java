package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class aoc21_1 {

    public static void main(String[] args) {
        List<Measurement> measurementList = load_measurements("data/aoc21_1.txt");
        Long increases = count_increases(measurementList);

        System.out.println("Found " + increases + " in pattern.");

        List<Measurement> movAvg = filter_mov_av(3, measurementList);
        Long increasesMovAvg = count_increases(movAvg);

        System.out.println("Found " + increasesMovAvg + " in filtered pattern.");

        System.out.println("done.");
    }

    private static List<Measurement> filter_mov_av(int filter_size, List<Measurement> measurementList) {
        List<Measurement> movAvg = new ArrayList<>();
        for (int i = 0; i < measurementList.size() - filter_size + 1; ++i) {
            Measurement meas;
            int new_val = 0;
            for (int j = 0; j < filter_size; ++j) {
                new_val += measurementList.get(i + j).value();
            }
            if (i == 0) {
                meas = new Measurement(new_val, 0);
            } else {
                meas = new Measurement(new_val, new_val - movAvg.get(i - 1).value());
            }
            movAvg.add(meas);
        }
        return movAvg;
    }

    private static Long count_increases(List<Measurement> measurementList) {
        Long increases = 0L;
        for (Measurement meas : measurementList) {
            if (meas.diff() > 0) {
                increases++;
            }
        }
        return increases;
    }

    private static List<Measurement> load_measurements(String pattern_path) {
        List<Measurement> measurementList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pattern_path))) {
            String line;
            int idx = 0;
            while ((line = br.readLine()) != null) {
                Measurement new_meas;
                if (idx == 0) {
                    new_meas = new Measurement(Integer.parseInt(line), 0);
                } else {
                    new_meas = new Measurement(Integer.parseInt(line), Integer.parseInt(line) - measurementList.get(idx - 1).value());
                }
                idx++;
                measurementList.add(new_meas);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return measurementList;
    }
}

record Measurement(int value, int diff) {
}