package ch.aoc21;

import com.google.common.collect.Sets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class aoc21_8 {
    public static void main(String[] args) {
        List<Entry> entries = parse_input("data/aoc21_8.txt");
        Long easy_digits = count_easy_digits(entries);

        System.out.println("Found " + easy_digits + " easy digits in set.\n");

        Long summed_digits = count_digits(entries);

        System.out.println("Summed digits: " + summed_digits + " .\n");

        System.out.println("done.");
    }

    private static Long count_digits(List<Entry> entries) {
        Long counter = 0L;
        for (Entry entry : entries) {
            counter += entry.get_four_digit();
        }
        return counter;
    }


    private static Long count_easy_digits(List<Entry> entries) {
        Long counter = 0L;
        for (Entry entry : entries) {
            counter += entry.count_1478();
        }
        return counter;
    }

    private static List<Entry> parse_input(String data_path) {
        List<Entry> entries = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] first_split = line.split(" \\| ");

                List<Set<Character>> sig_pat = new ArrayList<>();
                for (String digit : first_split[0].split(" ")) {
                    Set<Character> char_set = digit.chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
                    sig_pat.add(char_set);
                }
                List<Set<Character>> for_dig = new ArrayList<>();
                for (String digit : first_split[1].split(" ")) {
                    Set<Character> char_set = digit.chars().mapToObj(e -> (char) e).collect(Collectors.toSet());
                    for_dig.add(char_set);
                }
                Entry new_entry = new Entry(sig_pat, for_dig);
                entries.add(new_entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

}

class Entry {
    private final Map<Character, Character> mapping = new HashMap<>();
    private final Map<String, String> correct_mapping = new HashMap<>();
    public List<Set<Character>> signal_pattern;
    public List<Set<Character>> four_digit;

    public Entry(List<Set<Character>> sig_pat, List<Set<Character>> fo_dig) {
        signal_pattern = sig_pat;
        four_digit = fo_dig;
        decode();
        init_correct_mapping();
    }

    private void init_correct_mapping() {
        correct_mapping.put("abcefg", "0");
        correct_mapping.put("cf", "1");
        correct_mapping.put("acdeg", "2");
        correct_mapping.put("acdfg", "3");
        correct_mapping.put("bcdf", "4");
        correct_mapping.put("abdfg", "5");
        correct_mapping.put("abdefg", "6");
        correct_mapping.put("acf", "7");
        correct_mapping.put("abcdefg", "8");
        correct_mapping.put("abcdfg", "9");
    }

    private void decode() {
        Map<Integer, List<Set<Character>>> count_sorted = get_count_sorted();

        // get a
        count_sorted.get(3).get(0).removeAll(count_sorted.get(2).get(0));
        Character a = count_sorted.get(3).get(0).stream().toList().get(0);

        // get possible sets
        Set<Character> poss_c_f = count_sorted.get(2).get(0);
        count_sorted.get(4).get(0).removeAll(count_sorted.get(2).get(0));

        Set<Character> poss_b_d = count_sorted.get(4).get(0);

        Set<Character> abc_set = new HashSet<>();
        abc_set.add('a');
        abc_set.add('b');
        abc_set.add('c');
        abc_set.add('d');
        abc_set.add('e');
        abc_set.add('f');
        abc_set.add('g');

        abc_set.remove(a);
        abc_set.removeAll(poss_c_f);
        abc_set.removeAll(poss_b_d);

        Set<Character> poss_c_d_e = get_poss_c_d_e(count_sorted.get(6));

        Character c = Sets.intersection(poss_c_d_e, poss_c_f).stream().toList().get(0);
        poss_c_f.remove(c);
        Character f = poss_c_f.stream().toList().get(0);

        Character d = Sets.intersection(poss_c_d_e, poss_b_d).stream().toList().get(0);
        poss_b_d.remove(d);
        Character b = poss_b_d.stream().toList().get(0);

        Character e = Sets.intersection(poss_c_d_e, abc_set).stream().toList().get(0);
        abc_set.remove(e);
        Character g = abc_set.stream().toList().get(0);


        mapping.put(a, 'a');
        mapping.put(b, 'b');
        mapping.put(c, 'c');
        mapping.put(d, 'd');
        mapping.put(e, 'e');
        mapping.put(f, 'f');
        mapping.put(g, 'g');
    }

    private Set<Character> get_poss_c_d_e(List<Set<Character>> sets) {
        Set<Character> c_d_e = new HashSet<>();
        c_d_e.addAll(Sets.difference(sets.get(0), sets.get(1)));
        c_d_e.addAll(Sets.difference(sets.get(1), sets.get(0)));
        c_d_e.addAll(Sets.difference(sets.get(2), sets.get(1)));
        c_d_e.addAll(Sets.difference(sets.get(1), sets.get(2)));
        return c_d_e;
    }

    private Map<Integer, List<Set<Character>>> get_count_sorted() {
        Map<Integer, List<Set<Character>>> count_sorted = new HashMap<>();
        for (int i = 2; i < 8; ++i) {
            List<Set<Character>> new_list = new ArrayList<>();
            for (Set<Character> digit : signal_pattern) {
                if (digit.size() == i) {
                    new_list.add(digit);
                }
            }
            count_sorted.put(i, new_list);
        }
        return count_sorted;
    }

    public Long count_1478() {
        Long counts = 0L;
        for (Set<Character> digit4 : four_digit) {
            int new_count = digit4.size();
            if (new_count == 2 || new_count == 3 || new_count == 4 || new_count == 7) counts++;
        }
        return counts;
    }

    public Long get_four_digit() {
        StringBuilder long_string = new StringBuilder();
        for (Set<Character> digit : four_digit) {
            StringBuilder translated_digit = new StringBuilder();
            for (Character my_char : digit) {
                translated_digit.append(mapping.get(my_char));
            }
            String sorted_trans_digit = sort_string(translated_digit.toString());
            long_string.append(correct_mapping.get(sorted_trans_digit));
        }
        return Long.parseLong(long_string.toString());
    }

    private String sort_string(String translated_digit) {
        char[] tempArray = translated_digit.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }
}