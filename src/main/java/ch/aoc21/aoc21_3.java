package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class aoc21_3 {
    public static void main(String[] args) {
        List<Long> one_bits = read_bits("data/aoc21_3.txt");
        Long power_cons = calc_power_cons(one_bits);

        System.out.println("Power consumption: " + power_cons + ".\n");

        Long life_support_rating = calc_life_support("data/aoc21_3.txt");

        System.out.println("Life support rating: " + life_support_rating + ". ");

        System.out.println("done");

    }

    private static Long calc_life_support(String data_path) {
        List<String> bit_list = read_bit_list(data_path);
        List<String> oxy_str = calc_oxy_co2(bit_list, 0, "oxy");
        Long oxy = Long.parseLong(oxy_str.get(0), 2);

        bit_list = read_bit_list(data_path);
        List<String> co2_str = calc_oxy_co2(bit_list, 0, "co2");
        Long co2 = Long.parseLong(co2_str.get(0), 2);

        return co2 * oxy;
    }

    private static List<String> calc_oxy_co2(List<String> bit_list, int i, String crit) {
        if (bit_list.size() != 1) {
            List<String> one_list = new ArrayList<>();
            List<String> zero_list = new ArrayList<>();
            for (String bit_line : bit_list) {
                if (bit_line.charAt(i % 12) == '1') {
                    one_list.add(bit_line);
                } else {
                    zero_list.add(bit_line);
                }
            }

            if (crit.equals("oxy")) {
                if (one_list.size() >= zero_list.size()) {
                    return calc_oxy_co2(one_list, i + 1, crit);
                } else {
                    return calc_oxy_co2(zero_list, i + 1, crit);
                }
            } else if (crit.equals("co2")) {
                if (one_list.size() >= zero_list.size()) {
                    return calc_oxy_co2(zero_list, i + 1, crit);
                } else {
                    return calc_oxy_co2(one_list, i + 1, crit);
                }
            } else {
                throw new RuntimeException("error");
            }
        } else {
            return bit_list;
        }

    }

    private static List<String> read_bit_list(String s) {
        List<String> bits = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                bits.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bits;
    }

    private static Long calc_power_cons(List<Long> one_bits) {
        String gamma_set = "";
        String epsilon_set = "";
        for (Long bit : one_bits) {
            if (bit >= 500L) {
                gamma_set += "1";
                epsilon_set += "0";
            } else {
                gamma_set += "0";
                epsilon_set += "1";
            }
        }


        return Long.parseLong(gamma_set, 2) * Long.parseLong(epsilon_set, 2);
    }

    private static List<Long> read_bits(String s) {
        List<Long> one_bits = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            boolean init = false;
            while ((line = br.readLine()) != null) {
                if (!init) {
                    for (int i = 0; i < line.length(); ++i) {
                        Long bit = 0L;
                        if (line.charAt(i) == '1') {
                            bit++;
                        }
                        one_bits.add(bit);
                    }
                    init = true;
                } else {
                    for (int i = 0; i < line.length(); ++i) {
                        if (line.charAt(i) == '1') {
                            one_bits.set(i, one_bits.get(i) + 1L);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return one_bits;
    }
}

