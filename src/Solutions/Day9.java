package Solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day9 extends Solution {
    private static class Point {
        public int x;
        public int y;

        public Point(Stream<Integer> stream) {
            Integer[] coords = stream.toArray(Integer[]::new);
            this.x = coords[0];
            this.y = coords[1];
        }
    }

    private static class Line {
        public Point start;
        public Point end;

        public Line(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public boolean Intersects(Point p, Point q) {
            return !(start.x <= Math.min(p.x, q.x) && end.x <= Math.min(p.x, q.x))&&
                   !(start.x >= Math.max(p.x, q.x) && end.x >= Math.max(p.x, q.x))&&
                   !(start.y <= Math.min(p.y, q.y) && end.y <= Math.min(p.y, q.y))&&
                   !(start.y >= Math.max(p.y, q.y) && end.y >= Math.max(p.y, q.y));
        }
    }

    private boolean areaValid (Point p, Point q, ArrayList<Line> lines) {
        for (var line : lines) {
            if (line.Intersects(p, q)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var input = Arrays.stream(inputData.split("\n"))
                .map(x -> new Point(Arrays.stream(x.split(",")).map(Integer::parseInt)))
                .toArray(Point[]::new);

        var lines = new ArrayList<Line>();
        for (int i = 0; i < input.length - 1; i++) {
            lines.add(new Line(input[i], input[i + 1]));
        }
        lines.add(new Line(input[input.length - 1], input[0]));

        long max = 0;
        for (int i = 0; i < input.length - 1; i++) {
            Point p = input[i];
            for (int j = i + 1; j < input.length; j++) {
                System.out.print("\rProgress: " + String.format("%.2f", (double)(i * input.length + j) / (input.length * input.length) * 100) + "%");
                Point q = input[j];

                long area = (long) (Math.abs(p.x - q.x) + 1) * (Math.abs(p.y - q.y) + 1);

                if (!part2 || areaValid(p, q, lines)) {
                    if (area > max) {
                        max = area;
                    }
                }
            }
        }
        System.out.println("\n\nMax chair area: " + max);
    }
}
//1574684850