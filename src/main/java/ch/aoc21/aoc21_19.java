package ch.aoc21;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import com.google.common.math.LongMath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class aoc21_19 {
    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Scanner> scanners = parse_input("data/aoc21_19.txt");
        Map<Integer, List<PairTransform>> mappings = new HashMap<>();
        for (int i = scanners.size() - 1; i >= 0; --i) {
            for (int j = scanners.size() - 1; j >= 0; --j) {
                if (i != j) {
                    match_scanners(scanners.get(j), scanners.get(i), j, i, mappings);
                }
            }
        }
        Set<Integer> visited = new HashSet<>();
        visited.add(0);
        Set<Point3D> points = transform_all_points(0, scanners, mappings, visited);
        System.out.println("Part1; found " + points.size() + " points in total.\n");

        visited = new HashSet<>();
        visited.add(0);
        Set<Point3D> scanner_positions = get_scanner_position(0, mappings, visited);
        Long largest_manhatten_dist = calc_manhatten_distance(scanner_positions);
        System.out.println("Part2; largest manhatten dist " + largest_manhatten_dist + ".\n");


        stopwatch.stop();
        System.out.println("Time elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.\n");
        System.out.println("done.");
    }

    private static Long calc_manhatten_distance(Set<Point3D> scanner_positions) {
        List<Long> manhatten_distances = new ArrayList<>();
        for (int i = 0; i < scanner_positions.size(); ++i) {
            for (int j = 0; j < scanner_positions.size(); ++j) {
                manhatten_distances.add(calc_man(scanner_positions.stream().toList().get(i), scanner_positions.stream().toList().get(j)));
            }
        }
        return Collections.max(manhatten_distances);
    }

    private static Long calc_man(Point3D point3D, Point3D point3D1) {
        return abs(point3D.x() - point3D1.x()) + abs(point3D.y() - point3D1.y()) + abs(point3D.z() - point3D1.z());
    }

    private static Set<Point3D> get_scanner_position(int i, Map<Integer, List<PairTransform>> mappings, Set<Integer> visited) {
        Set<Point3D> scanner_positions = new HashSet<>();
        Point3D origin = new Point3D(0, 0, 0);
        scanner_positions.add(origin);
        List<PairTransform> transforms = mappings.get(i);
        for (PairTransform transform : transforms) {
            if (!visited.contains(transform.source())) {
                visited.add(transform.source());
                Set<Point3D> transformed = get_scanner_position(transform.source(), mappings, visited);
                for (Point3D point : transformed) {
                    scanner_positions.add(transform_point(point, transform.transform()));
                }
            }
        }
        return scanner_positions;
    }

    private static Set<Point3D> transform_all_points(int i, List<Scanner> scanners, Map<Integer, List<PairTransform>> mappings, Set<Integer> visited) {
        Set<Point3D> points_0 = scanners.get(i).points();
        List<PairTransform> transforms = mappings.get(i);
        for (PairTransform transform : transforms) {
            if (!visited.contains(transform.source())) {
                visited.add(transform.source());
                Set<Point3D> transformed = transform_all_points(transform.source(), scanners, mappings, visited);
                for (Point3D point : transformed) {
                    points_0.add(transform_point(point, transform.transform()));
                }
            }
        }
        return points_0;
    }


    private static void match_scanners(Scanner scanner0, Scanner scanner1, int to, int from, Map<Integer, List<PairTransform>> mappings) {
        Map<Point3D, Point3D> matching = new HashMap<>();
        for (Point3D point0 : scanner0.points()) {
            if (matching.keySet().size() >= 10) {
                break;
            } else {
                Set<Long> neigh_dist0 = get_neighbour_dist(point0, scanner0);
                for (Point3D point1 : scanner1.points()) {
                    Set<Long> neigh_dist1 = get_neighbour_dist(point1, scanner1);
                    Set<Long> intersection = Sets.intersection(neigh_dist0, neigh_dist1);
                    if (intersection.size() > 6) {
                        matching.put(point0, point1);
                    }
                }
            }
        }
        if (matching.keySet().size() >= 10) {
            Tensor transform_10 = calc_transform(matching);
            if (check_transform(transform_10)) {
                if (mappings.containsKey(to)) {
                    mappings.get(to).add(new PairTransform(from, transform_10));
                } else {
                    List<PairTransform> new_list = new ArrayList<>();
                    new_list.add(new PairTransform(from, transform_10));
                    mappings.put(to, new_list);
                }
            }
        }
    }

    private static boolean check_transform(Tensor transform) {
        int zeros = 0;
        for (int i = 0; i < 12; ++i) {
            if (transform.Get(i, 0).number().longValue() == 0L) {
                zeros++;
            }
        }
        return zeros >= 6;
    }

    private static Point3D transform_point(Point3D point, Tensor transform) {
        Tensor A = Array.zeros(3, 12);
        A.set(Tensors.vector(point.x(), point.y(), point.z(), 1, 0, 0, 0, 0, 0, 0, 0, 0), 0);
        A.set(Tensors.vector(0, 0, 0, 0, point.x(), point.y(), point.z(), 1, 0, 0, 0, 0), 1);
        A.set(Tensors.vector(0, 0, 0, 0, 0, 0, 0, 0, point.x(), point.y(), point.z(), 1), 2);

        Tensor new_point = A.dot(transform);

        return new Point3D(new_point.Get(0, 0).number().longValue(), new_point.Get(1, 0).number().longValue(), new_point.Get(2, 0).number().longValue());
    }

    private static Tensor calc_transform(Map<Point3D, Point3D> matching) {
        Tensor A = Array.zeros(3 * matching.keySet().size(), 12);
        Tensor b = Array.zeros(3 * matching.keySet().size(), 1);
        int i = 0;
        for (Point3D point0 : matching.keySet()) {
            Point3D point1 = matching.get(point0);
            A.set(Tensors.vector(point1.x(), point1.y(), point1.z(), 1, 0, 0, 0, 0, 0, 0, 0, 0), i);
            b.set(Tensors.vector(point0.x()), i);
            i++;
            A.set(Tensors.vector(0, 0, 0, 0, point1.x(), point1.y(), point1.z(), 1, 0, 0, 0, 0), i);
            b.set(Tensors.vector(point0.y()), i);
            i++;
            A.set(Tensors.vector(0, 0, 0, 0, 0, 0, 0, 0, point1.x(), point1.y(), point1.z(), 1), i);
            b.set(Tensors.vector(point0.z()), i);
            i++;
        }
        return PseudoInverse.of(A).dot(b);
    }

    private static Set<Long> get_neighbour_dist(Point3D point, Scanner scanner) {
        Set<Long> distances = new HashSet<>();
        for (Point3D comp_point : scanner.points()) {
            if (!point.equals(comp_point)) {
                distances.add(calc_dist(point, comp_point));
            }
        }
        return distances;
    }

    private static Long calc_dist(Point3D point, Point3D comp_point) {
        return LongMath.sqrt(LongMath.pow(point.x() - comp_point.x(), 2) + LongMath.pow(point.y() - comp_point.y(), 2) + LongMath.pow(point.z() - comp_point.z(), 2), RoundingMode.HALF_UP);
    }

    private static List<Scanner> parse_input(String data_path) {
        List<Scanner> scanners = new ArrayList<>();
        Set<Point3D> points = new HashSet<>();
        boolean init_new_scanner = false;
        try (BufferedReader br = new BufferedReader(new FileReader(data_path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    scanners.add(new Scanner(points));
                    points = new HashSet<>();
                    init_new_scanner = false;
                } else if (init_new_scanner) {
                    String[] split = line.split(",");
                    points.add(new Point3D(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2])));
                } else {
                    init_new_scanner = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scanners.add(new Scanner(points));
        return scanners;
    }
}

record Point3D(long x, long y, long z) {
}

record Scanner(Set<Point3D> points) {
}

record PairTransform(int source, Tensor transform) {
}