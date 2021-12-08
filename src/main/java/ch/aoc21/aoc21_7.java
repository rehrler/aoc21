package ch.aoc21;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class aoc21_7 {
    public static void main(String[] args) {
        List<Long> positions = parse_positions("data/aoc21_7.txt");
        Long line_value = (long) get_line_value(positions);
        //Long mean = positions.stream().mapToLong(a -> a).average();
        long fuel_consumption = positions.stream().map(i -> Math.abs(i - line_value)).toList().stream().mapToLong(a -> a).sum();

        System.out.println("Fuel consumption: " + fuel_consumption + ". \n");

        Long try_hard1 = solve_task1(positions);

        System.out.println("Try hard 1: " + try_hard1);

        Long try_hard2 = solve_task2(positions);

        System.out.println("Try hard 2: " + try_hard2);

        System.out.println("done. ");
    }

    private static Long solve_task2(List<Long> positions) {
        List<Long> try_hard1 = new ArrayList<>();
        for (long k = 0L; k < 900L; ++k) {
            Long finalK = k;
            Long new_value = positions.stream()
                    .map(i -> Math.abs(i - finalK) * (1L + Math.abs(i - finalK)))
                    .mapToLong(a -> a).sum();
            try_hard1.add(new_value);
        }
        return Collections.min(try_hard1) / 2L;
    }

    private static Long solve_task1(List<Long> positions) {
        List<Long> try_hard1 = new ArrayList<>();
        for (long k = 0L; k < 900L; ++k) {
            Long finalK = k;
            Long new_value = positions.stream()
                    .map(i -> Math.abs(i - finalK))
                    .mapToLong(a -> a).sum();
            try_hard1.add(new_value);
        }
        return Collections.min(try_hard1);
    }

    private static double get_line_value(List<Long> positions) {
        Loader.loadNativeLibraries();

        MPSolver solver = MPSolver.createSolver("GLOP");

        double inf = Double.POSITIVE_INFINITY;

        // create coeff
        List<MPVariable> variables = new ArrayList<>();
        int idx = 0;
        for (Long ignored : positions) {
            MPVariable t_i = solver.makeNumVar(0.0, inf, "t_" + idx);
            variables.add(t_i);
            idx++;
        }
        MPVariable f = solver.makeNumVar(0.0, inf, "f");
        variables.add(f);

        // constraints
        idx = 0;
        for (Long position : positions) {
            MPConstraint constraint = solver.makeConstraint(-inf, -position, "c_0_" + idx);
            constraint.setCoefficient(variables.get(idx), -1.0);
            constraint.setCoefficient(variables.get(variables.size() - 1), -1.0);
            MPConstraint constraint2 = solver.makeConstraint(-inf, position, "c_1_" + idx);
            constraint2.setCoefficient(variables.get(idx), -1.0);
            constraint2.setCoefficient(variables.get(variables.size() - 1), 1.0);
            idx++;
        }

        // objective
        MPObjective objective = solver.objective();
        for (int i = 0; i < variables.size() - 1; ++i) {
            objective.setCoefficient(variables.get(i), 1.0);
        }

        // solve
        final MPSolver.ResultStatus resultStatus = solver.solve();

        return variables.get(variables.size() - 1).solutionValue();

    }

    private static List<Long> parse_positions(String data_path) {
        List<Long> positions = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                for (String str_nb : split) {
                    positions.add(Long.parseLong(str_nb));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return positions;
    }
}
