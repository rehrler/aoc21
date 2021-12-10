package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class aoc21_10 {
    public static void main(String[] args) {
        List<String> brackets = parse_input("data/aoc21_10.txt");
        BracketAnalyzer analyzer = new BracketAnalyzer();

        Long score_1 = 0L;
        for (String bracket_line : brackets) {
            score_1 += analyzer.check_brackets(bracket_line);
        }
        System.out.println("Score for part1: " + score_1 + ".\n");

        List<Long> scores_part2 = new ArrayList<>();
        for (String bracket_line : brackets) {
            Long new_score = analyzer.calc_score2(bracket_line);
            if (new_score != 0L) {
                scores_part2.add(new_score);
            }
        }

        Collections.sort(scores_part2);

        Long score_2 = scores_part2.get(scores_part2.size() / 2);
        System.out.println("Score for part2: " + score_2 + ".\n");

        System.out.println("done");
    }

    private static List<String> parse_input(String data_path) {
        List<String> bracket_lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                bracket_lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bracket_lines;
    }
}

class BracketAnalyzer {
    private final List<Character> left_brackets = Arrays.asList('(', '[', '{', '<');
    private final List<Character> right_brackets = Arrays.asList(')', ']', '}', '>');
    private final Map<Character, Long> score_table1 = new HashMap<>();
    private final Map<Character, Long> score_table2 = new HashMap<>();

    public BracketAnalyzer() {
        score_table1.put(')', 3L);
        score_table1.put(']', 57L);
        score_table1.put('}', 1197L);
        score_table1.put('>', 25137L);
        score_table1.put('(', 0L);
        score_table1.put('[', 0L);
        score_table1.put('{', 0L);
        score_table1.put('<', 0L);

        score_table2.put(')', 1L);
        score_table2.put(']', 2L);
        score_table2.put('}', 3L);
        score_table2.put('>', 4L);
    }

    public Long check_brackets(String bracket_str) {
        return calc_score(bracket_str);
    }

    private Long calc_score(String bracket_str) {
        Stack<Character> left_bracket_stack = new Stack<>();

        for (char bracket : bracket_str.toCharArray()) {
            if (left_brackets.contains(bracket)) {
                left_bracket_stack.push(bracket);
            }

            if (right_brackets.contains(bracket)) {
                if (left_bracket_stack.empty()) {
                    return score_table1.get(bracket);
                }

                Character poss_opening_bracket = left_bracket_stack.pop();
                if (left_brackets.indexOf(poss_opening_bracket) != right_brackets.indexOf(bracket)) {
                    return score_table1.get(bracket);
                }
            }
        }
        return 0L;
    }

    public Long calc_score2(String bracket_line) {
        Stack<Character> left_bracket_stack = new Stack<>();

        for (char bracket : bracket_line.toCharArray()) {
            if (left_brackets.contains(bracket)) {
                left_bracket_stack.push(bracket);
            }

            if (right_brackets.contains(bracket)) {
                if (left_bracket_stack.empty()) {
                    return 0L;
                }

                Character poss_opening_bracket = left_bracket_stack.pop();
                if (left_brackets.indexOf(poss_opening_bracket) != right_brackets.indexOf(bracket)) {
                    return 0L;
                }
            }
        }

        Long score2 = 0L;
        while (!left_bracket_stack.empty()) {
            score2 *= 5L;

            score2 += score_table2.get(right_brackets.get(left_brackets.indexOf(left_bracket_stack.pop())));
        }
        return score2;
    }
}
