package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class aoc21_14 {


    public static void main(String[] args) {
        String polymer_template = load_template("data/aoc21_14.txt");
        Map<String, String> insertion_rules = load_insertions("data/aoc21_14.txt");

        Map<String, Long> extended_poly = extend_poly(polymer_template, insertion_rules, 10);
        Long score1 = calc_score(extended_poly, polymer_template);
        System.out.println("Part1; score is " + score1 + ".\n");

        extended_poly = extend_poly(polymer_template, insertion_rules, 40);
        Long score2 = calc_score(extended_poly, polymer_template);
        System.out.println("Part2; score is " + score2 + ".\n");

        System.out.println("done.");
    }

    private static Long calc_score(Map<String, Long> extended_poly, String polymer_template) {
        Map<Character, Long> counts = new HashMap<>();
        for (String key : extended_poly.keySet()) {
            Character char1 = key.charAt(0);
            Character char2 = key.charAt(1);
            add_char(char1, extended_poly.get(key), counts);
            add_char(char2, extended_poly.get(key), counts);
        }
        for (Character key : counts.keySet()) {
            if (key == polymer_template.charAt(0) || key == polymer_template.charAt(polymer_template.length() - 1)) {
                counts.put(key, (counts.get(key) + 1L) / 2);
            } else {
                counts.put(key, counts.get(key) / 2);
            }
        }
        List<Long> counts_listed = new ArrayList<>(counts.values());
        return Collections.max(counts_listed) - Collections.min(counts_listed);
    }

    private static void add_char(Character character, Long value, Map<Character, Long> counts) {
        if (counts.containsKey(character)) {
            counts.put(character, counts.get(character) + value);
        } else {
            counts.put(character, value);
        }
    }


    private static Map<String, Long> extend_poly(String polymer_template, Map<String, String> insertion_rules, int repetitions) {
        Map<String, Long> old_poly;
        Map<String, Long> new_poly = build_map_from_string(polymer_template);
        while (repetitions != 0) {
            old_poly = new HashMap<>(new_poly);
            new_poly = update_poly(old_poly, insertion_rules);
            repetitions--;
        }
        return new_poly;
    }

    private static Map<String, Long> update_poly(Map<String, Long> old_poly, Map<String, String> insertion_rules) {
        Map<String, Long> map = new HashMap<>();
        for (String key : old_poly.keySet()) {
            String between_char = insertion_rules.get(key);
            String new_poly1 = key.charAt(0) + between_char;
            String new_poly2 = between_char + key.charAt(1);
            add_poly(new_poly1, old_poly.get(key), map);
            add_poly(new_poly2, old_poly.get(key), map);
        }
        return map;
    }

    private static void add_poly(String new_poly, Long value, Map<String, Long> map) {
        if (map.containsKey(new_poly)) {
            map.put(new_poly, map.get(new_poly) + value);
        } else {
            map.put(new_poly, value);
        }
    }

    private static Map<String, Long> build_map_from_string(String polymer_template) {
        Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < polymer_template.length() - 1; ++i) {
            String sub_string = polymer_template.substring(i, i + 2);
            if (map.containsKey(sub_string)) {
                map.put(sub_string, map.get(sub_string) + 1L);
            } else {
                map.put(sub_string, 1L);
            }
        }
        return map;
    }

    private static String load_template(String data_path) {
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    return line;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> load_insertions(String data_path) {
        Map<String, String> insertions = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            boolean init = false;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    init = true;
                } else {
                    if (init) {
                        String[] split = line.split(" -> ");
                        insertions.put(split[0], split[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insertions;
    }
}
