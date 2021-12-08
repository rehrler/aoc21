package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class aoc21_2 {

    public static void main(String[] args) {
        List<Command> commands = load_commands("data/aoc21_2.txt");

        Long pos_dep = calc_pos_dep(commands);
        System.out.println("Part 1: pos*depth = " + pos_dep + ".\n");

        Long pos_dep2 = calc_pos_dep_aim(commands);
        System.out.println("Part 2: pos*depth = " + pos_dep2 + ".\n");

        System.out.println("done");
    }

    private static Long calc_pos_dep_aim(List<Command> commands) {
        long pos = 0L;
        long depth = 0L;
        long aim = 0L;

        for (Command command : commands) {
            if (Objects.equals(command.direction(), "forward")) {
                pos += command.steps();
                depth += aim * (long) command.steps();
            } else if (Objects.equals(command.direction(), "down")) {
                aim += command.steps();
            } else if (Objects.equals(command.direction(), "up")) {
                aim -= command.steps();
            } else {
                throw new RuntimeException("wrong command with " + command.direction());
            }
        }

        return pos * depth;
    }

    private static Long calc_pos_dep(List<Command> commands) {
        long pos = 0L;
        long depth = 0L;
        for (Command command : commands) {
            if (Objects.equals(command.direction(), "forward")) {
                pos += command.steps();
            } else if (Objects.equals(command.direction(), "down")) {
                depth += command.steps();
            } else if (Objects.equals(command.direction(), "up")) {
                depth -= command.steps();
            } else {
                throw new RuntimeException("wrong command with " + command.direction());
            }
        }
        return pos * depth;
    }

    private static List<Command> load_commands(String data_path) {
        List<Command> commands = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                Command command = new Command(split[0], Integer.parseInt(split[1]));
                commands.add(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commands;
    }
}

record Command(String direction, int steps) {
}