package ch.aoc21;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class aoc21_5 {
    public static void main(String[] args) {
        List<Line> lines = parse_lines("data/aoc21_5.txt");
        int[][] field = get_field(lines);

        Long overlaps = count_overlaps(field, lines);

        System.out.println("Found " + overlaps + " overlaps greater or equal than 2.");

        System.out.println("done.");
    }

    private static Long count_overlaps(int[][] field, List<Line> lines) {
        for (Line line : lines) {
            if (line.check_horiz_vert()) {
                List<Point> fields = line.get_fields();
                for (Point point : fields) {
                    field[point.y() - 1][point.x() - 1] += 1;
                }
            }
        }
        long counter = 0L;
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                if (field[i][j] >= 2) {
                    counter += 1L;
                }
            }
        }
        return counter;
    }

    private static int[][] get_field(List<Line> lines) {
        int[] x_min_max_y_min_max = find_minmax(lines);

        int[][] field = new int[x_min_max_y_min_max[1]][x_min_max_y_min_max[3]];

        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[0].length; ++j) {
                field[i][j] = 0;
            }
        }
        return field;
    }

    private static int[] find_minmax(List<Line> lines) {
        int x_min = 0;
        int x_max = 0;
        int y_min = 0;
        int y_max = 0;
        for (Line line : lines) {
            if (line.getStart_point().x() < x_min) {
                x_min = line.getStart_point().x();
            }
            if (line.getStart_point().x() > x_max) {
                x_max = line.getStart_point().x();
            }
            if (line.getEnd_point().x() < x_min) {
                x_min = line.getEnd_point().x();
            }
            if (line.getEnd_point().x() > x_max) {
                x_max = line.getEnd_point().x();
            }
            if (line.getStart_point().y() < y_min) {
                y_min = line.getStart_point().y();
            }
            if (line.getStart_point().y() > y_max) {
                y_max = line.getStart_point().y();
            }
            if (line.getEnd_point().y() < y_min) {
                y_min = line.getEnd_point().y();
            }
            if (line.getEnd_point().y() > y_max) {
                y_max = line.getEnd_point().y();
            }
        }
        return new int[]{x_min, x_max, y_min, y_max};
    }

    private static List<Line> parse_lines(String data_path) {
        List<Line> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] first_split = line.split(" -> ");
                String[] start_point = first_split[0].split(",");
                String[] end_point = first_split[1].split(",");
                Line new_line = new Line(Integer.parseInt(start_point[0]), Integer.parseInt(start_point[1]), Integer.parseInt(end_point[0]), Integer.parseInt(end_point[1]));
                lines.add(new_line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}

record Point(int x, int y) {
}

class Line {
    private final Point start_point;
    private final Point end_point;
    private boolean diag = false;
    private boolean vert = false;
    private boolean horiz = false;

    public Line(int x1, int y1, int x2, int y2) {
        start_point = new Point(x1, y1);
        end_point = new Point(x2, y2);
        check_orientation();
    }

    private void check_orientation() {
        if (start_point.x() == end_point.x()) {
            vert = true;
        }
        if (start_point.y() == end_point.y()) {
            horiz = true;
        }

        if (Math.abs(start_point.x() - end_point.x()) == Math.abs(start_point.y() - end_point.y())) {
            diag = true;
        }


    }

    public Point getEnd_point() {
        return end_point;
    }

    public Point getStart_point() {
        return start_point;
    }

    public boolean check_horiz_vert() {
        return horiz || vert || diag;
    }


    public List<Point> get_fields() {
        List<Point> fields = new ArrayList<>();
        int dx, dy;
        if (horiz) {
            dx = (end_point.x() - start_point.x()) > 0 ? 1 : -1;
            dy = 0;
        } else if (vert) {
            dx = 0;
            dy = (end_point.y() - start_point.y()) > 0 ? 1 : -1;
        } else if (diag) {
            dx = (end_point.x() - start_point.x()) > 0 ? 1 : -1;
            dy = (end_point.y() - start_point.y()) > 0 ? 1 : -1;
        } else {
            throw new RuntimeException("not possible here");
        }
        int curr_x = start_point.x();
        int curr_y = start_point.y();
        while (!((curr_x == end_point.x()) && (curr_y == end_point.y()))) {
            Point new_point = new Point(curr_x, curr_y);
            fields.add(new_point);
            curr_x += dx;
            curr_y += dy;
        }
        fields.add(end_point);
        return fields;
    }
}