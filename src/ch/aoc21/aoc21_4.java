package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class aoc21_4 {
    public static void main(String[] args) {
        List<Long> bingo_numbers = load_bingo_numbers("data/aoc21_4.txt");
        List<BingoBoard> bingo_boards = load_bingo_boards("data/aoc21_4.txt");

        // part1
        // Long score = play_bingo(bingo_numbers, bingo_boards);

        // part 2
        Long score = play_bingo_2part(bingo_numbers, bingo_boards);

        System.out.println("bingo score: " + score + ".\n");

        System.out.println("done.");
    }

    private static Long play_bingo_2part(List<Long> bingo_numbers, List<BingoBoard> bingo_boards) {
        for (Long bingo_number : bingo_numbers) {
            List<BingoBoard> boards_to_remove = new ArrayList<>();
            for (BingoBoard bingo_board : bingo_boards) {
                bingo_board.draw_number(bingo_number);
                Long score = bingo_board.get_score(bingo_number);
                if (!score.equals(0L)) {
                    if (bingo_boards.size() != 1) {
                        boards_to_remove.add(bingo_board);
                    } else {
                        return score;
                    }
                }
            }
            bingo_boards.removeAll(boards_to_remove);
        }
        return 0L;
    }

    private static Long play_bingo(List<Long> bingo_numbers, List<BingoBoard> bingo_boards) {
        for (Long bingo_number : bingo_numbers) {
            for (BingoBoard bingo_board : bingo_boards) {
                bingo_board.draw_number(bingo_number);
                Long score = bingo_board.get_score(bingo_number);
                if (!score.equals(0L)) {
                    return score;
                }
            }
        }
        return 0L;
    }

    private static List<BingoBoard> load_bingo_boards(String data_path) {
        List<BingoBoard> bingoBoards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            int line_idx = 0;
            int board_row_idx = 0;
            BingoBoard new_board = new BingoBoard(5);
            while ((line = br.readLine()) != null) {
                if (line_idx > 1) {
                    if (line.equals("")) {
                        bingoBoards.add(new_board);
                        new_board = new BingoBoard(5);
                        board_row_idx = 0;
                    } else {
                        String[] split = line.split(" ");
                        int col_idx = 0;
                        for (String number : split) {
                            if (!number.equals("")) {
                                new_board.add_number(Long.parseLong(number), board_row_idx, col_idx);
                                col_idx++;
                            }
                        }
                        board_row_idx++;
                    }
                }

                line_idx++;
            }
            bingoBoards.add(new_board);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bingoBoards;
    }

    private static List<Long> load_bingo_numbers(String data_path) {
        List<Long> bingo_numbers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            boolean init = false;
            while ((line = br.readLine()) != null) {
                if (!init) {
                    init = true;
                    String[] split = line.split(",");
                    for (String number : split) {
                        bingo_numbers.add(Long.parseLong(number));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bingo_numbers;
    }


}

class BingoBoard {
    private final Boolean[][] check;
    private final Long[][] numbers;
    private boolean checked = false;

    public BingoBoard(int s) {
        check = new Boolean[s][s];
        fill_arr(check, false);
        numbers = new Long[s][s];
        fill_arr(numbers, 0L);
    }

    public void add_number(Long new_number, int i, int j) {
        numbers[i][j] = new_number;
    }

    public void draw_number(Long drawn_number) {
        for (int i = 0; i < numbers.length; ++i) {
            for (int j = 0; j < numbers[0].length; ++j) {
                if (Objects.equals(numbers[i][j], drawn_number)) {
                    check[i][j] = true;
                }
            }
        }
    }

    public Long get_score(Long drawn_number) {
        check_check();
        if (checked) {
            return calc_score() * drawn_number;
        } else {
            return 0L;
        }
    }

    private Long calc_score() {
        Long score = 0L;
        for (int i = 0; i < numbers.length; ++i) {
            for (int j = 0; j < numbers[0].length; ++j) {
                if (!check[i][j]) {
                    score += numbers[i][j];
                }
            }
        }
        return score;
    }

    private void check_check() {
        // check rows
        for (int i = 0; i < numbers.length; ++i) {
            boolean row_check = true;
            for (int j = 0; j < numbers[0].length; ++j) {
                if (!check[i][j]) {
                    row_check = false;
                    break;
                }
            }
            if (row_check) {
                checked = row_check;
            }
        }

        // check columns
        for (int j = 0; j < numbers[0].length; ++j) {
            boolean col_check = true;
            for (int i = 0; i < numbers.length; ++i) {
                if (!check[i][j]) {
                    col_check = false;
                    break;
                }
            }
            if (col_check) {
                checked = col_check;
            }
        }
    }

    private void fill_arr(Object[][] numbers, Object l) {
        for (int i = 0; i < numbers.length; ++i) {
            for (int j = 0; j < numbers[0].length; ++j) {
                numbers[i][j] = l;
            }
        }
    }
}
