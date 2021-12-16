package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class aoc21_16 {
    private static int previous_i = 0;

    public static void main(String[] args) {
        String org_str = parse_string("data/aoc21_16.txt");
        Long sum_version_nb = parse_packets(org_str);
        System.out.println("Part1; sum of version numbers: " + sum_version_nb + ".\n");

        List<Long> hex_exp = parse_hex_exp(org_str, 10000);
        System.out.println("Part2; expression of hex: " + hex_exp.get(0) + ".\n");

        System.out.println("done.");
    }

    private static Long parse_packets(String org_str) {
        Long sum_version_nb = 0L;
        int i = 0;
        while (i < org_str.length()) {
            if (org_str.substring(i).chars().allMatch(e -> e == '0')) {
                break;
            }
            int version = Integer.parseInt(org_str.substring(i, i + 3), 2);
            int id = Integer.parseInt(org_str.substring(i + 3, i + 6), 2);
            sum_version_nb += (long) version;
            i += 6;
            if (id == 4) {
                for (; ; i += 5) {
                    if (org_str.charAt(i) == '0') {
                        i += 5;
                        break;
                    }
                }
            } else {
                int length_information = 15;
                if (org_str.charAt(i) == '1') {
                    length_information = 11;
                }
                i++;
                int length = Integer.parseInt(org_str.substring(i, i + length_information), 2);
                i += length_information;
                if (org_str.charAt(i - 1 - length_information) != '1') {
                    sum_version_nb += parse_packets(org_str.substring(i, i + length));
                    i += length;
                }
            }
        }

        return sum_version_nb;
    }

    private static Long operator(List<Long> values, int identifier) {
        return switch (identifier) {
            case 0 -> values.stream().mapToLong(l -> l).sum();
            case 1 -> values.stream().reduce(1L, (a, b) -> a * b);
            case 2 -> values.stream().mapToLong(l -> l).min().getAsLong();
            case 3 -> values.stream().mapToLong(l -> l).max().getAsLong();
            case 5 -> values.get(0) > values.get(1) ? 1L : 0L;
            case 6 -> values.get(0) < values.get(1) ? 1L : 0L;
            case 7 -> Objects.equals(values.get(0), values.get(1)) ? 1L : 0L;
            default -> throw new RuntimeException("wrong id");
        };
    }

    private static String parse_string(String data_path) {
        Map<Character, String> decoder = new HashMap<>();
        decoder.put('0', "0000");
        decoder.put('1', "0001");
        decoder.put('2', "0010");
        decoder.put('3', "0011");
        decoder.put('4', "0100");
        decoder.put('5', "0101");
        decoder.put('6', "0110");
        decoder.put('7', "0111");
        decoder.put('8', "1000");
        decoder.put('9', "1001");
        decoder.put('A', "1010");
        decoder.put('B', "1011");
        decoder.put('C', "1100");
        decoder.put('D', "1101");
        decoder.put('E', "1110");
        decoder.put('F', "1111");
        StringBuilder total_str = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                total_str.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder bin_str = new StringBuilder();
        for (Character hex_char : total_str.toString().toCharArray()) {
            bin_str.append(decoder.get(hex_char));
        }
        return bin_str.toString();
    }

    private static List<Long> parse_hex_exp(String org_str, int remaining_parsing) {
        List<Long> current_entries = new ArrayList<>();
        int i = 0;
        for (int parsed = 0; i < org_str.length(); ++parsed) {
            if (parsed >= remaining_parsing) {
                break;
            }
            if (org_str.substring(i).chars().allMatch(e -> e == '0')) break;
            int id = Integer.parseInt(org_str.substring(i + 3, i + 6), 2);
            i += 6;
            if (id == 4) {
                StringBuilder literal_number = new StringBuilder();
                for (; ; i += 5) {
                    literal_number.append(org_str, i + 1, i + 5);
                    if (org_str.charAt(i) == '0') {
                        i += 5;
                        break;
                    }
                }
                current_entries.add(Long.parseLong(literal_number.toString(), 2));
            } else {
                int length_information = 15;
                boolean operator_decoding_case = org_str.charAt(i) == '1';
                if (operator_decoding_case) {
                    length_information = 11;
                }
                i++;
                int length = Integer.parseInt(org_str.substring(i, i + length_information), 2);
                i += length_information;
                int next_idx;
                int next_limit;
                if (operator_decoding_case) {
                    next_idx = org_str.length();
                    next_limit = length;
                } else {
                    next_idx = i + length;
                    next_limit = 10000;
                }
                List<Long> current_entries2 = parse_hex_exp(org_str.substring(i, next_idx), next_limit);
                current_entries.add(operator(current_entries2, id));
                i += operator_decoding_case ? previous_i : length;
            }
            previous_i = i;
        }

        return current_entries;
    }
}