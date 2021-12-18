package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class aoc21_18 {
    public static void main(String[] args) {
        List<String> fishes = parse_input("data/aoc21_18.txt");

        Long magnitude = part1(fishes);
        System.out.println("Part1; magnitude of sum of snailfishes: " + magnitude + ".\n");

        Long highest_mag = part2(fishes);
        System.out.println("Part2; highest magnitude of two snailfishes: " + highest_mag + ".\n");

        System.out.println("done.");
    }

    private static Long part2(List<String> fishes) {
        long highest_mag = 0L;
        for (int i = 0; i < fishes.size(); ++i) {
            for (int j = 0; j < fishes.size(); ++j) {
                if (j != i) {
                    String added_fishes = add_fishes(fishes.get(i), fishes.get(j));
                    Long mag = calc_magnitude(added_fishes, new AtomicInteger(0));
                    if (mag > highest_mag) {
                        highest_mag = mag;
                    }
                }
            }
        }
        return highest_mag;
    }

    private static Long part1(List<String> fishes) {
        String result = fishes.get(0);
        for (int i = 1; i < fishes.size(); ++i) {
            result = add_fishes(result, fishes.get(i));
        }

        return calc_magnitude(result, new AtomicInteger(0));
    }

    private static Long calc_magnitude(String result, AtomicInteger pos) {
        if (result.charAt(pos.get()) == '[') {
            pos.incrementAndGet();
            Long value_left = calc_magnitude(result, pos);
            pos.incrementAndGet();
            Long value_right = calc_magnitude(result, pos);
            pos.incrementAndGet();
            return value_left * 3 + value_right * 2;
        } else {
            pos.incrementAndGet();
            return Long.valueOf(result.charAt(pos.get() - 1) - '0');
        }
    }

    private static String add_fishes(String result, String add) {
        String added_string = "[" + result + "," + add + "]";
        String reduced_string = reduce_fish(added_string);
        while (!Objects.equals(added_string, reduced_string)) {
            added_string = reduced_string;
            reduced_string = reduce_fish(added_string);
        }
        return reduced_string;
    }

    private static String reduce_fish(String fish) {
        int depth = 0;
        Pattern single_pair = Pattern.compile("\\[(\\d+),(\\d+)\\].*?");
        // check for explode
        for (int i = 0; i < fish.length(); ++i) {
            Matcher matcher = single_pair.matcher(fish.substring(i));
            if (depth >= 4 && matcher.matches()) {
                return explode_fish(fish, i, matcher);
            }
            if (fish.charAt(i) == '[') depth++;
            if (fish.charAt(i) == ']') depth--;
        }
        // check for split
        for (int pos = 0; pos < fish.length(); pos++) {
            if (isDigit(fish, pos) && peek_regular(fish, pos) > 9) {
                return split_fish(fish, pos);
            }
        }
        return fish;
    }

    private static String split_fish(String fish, int pos) {
        int regular = peek_regular(fish, pos);
        return fish.substring(0, pos) + "[" + regular / 2 + "," + (regular / 2 + regular % 2) + "]" + fish.substring(pos + 2);
    }

    private static String explode_fish(String fish, int position, Matcher matcher) {
        int left_value = Integer.parseInt(matcher.group(1));
        int right_value = Integer.parseInt(matcher.group(2));
        String result = fish.substring(0, position) + "0" + fish.substring(position + matcher.end(2) + 1);
        int p = position + 1;
        while (p < result.length() && !isDigit(result, p)) p++;
        if (p < result.length()) {
            result = replace_regular(result, p, peek_regular(result, p) + right_value);
        }
        p = position - 1;
        while (p > 0 && !isDigit(result, p)) p--;
        if (p > 0) {
            result = replace_regular(result, p, peek_regular(result, p) + left_value);
        }
        return result;
    }

    private static String replace_regular(String result, int p, int i) {
        while (p > 0 && isDigit(result, p - 1)) p--;
        int l = 1;
        while (p + l < result.length() && isDigit(result, p + l)) l++;
        return result.substring(0, p) + i + result.substring(p + l);
    }

    private static int peek_regular(String fish, int p) {
        while (p > 0 && isDigit(fish, p - 1)) p--;
        int result = fish.charAt(p) - '0';
        p++;
        while (p < fish.length() && isDigit(fish, p)) {
            result = result * 10 + (fish.charAt(p) - '0');
            p++;
        }
        return result;
    }

    private static boolean isDigit(String result, int p) {
        return result.charAt(p) >= '0' && result.charAt(p) <= '9';
    }

    private static List<String> parse_input(String data_path) {
        List<String> fishes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                fishes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fishes;
    }
}
