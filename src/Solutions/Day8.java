package Solutions;

import java.util.*;
import java.util.stream.Stream;

public class Day8 extends Solution {
    private class Coord {
        public int x;
        public int y;
        public int z;

        public Coord(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Coord(Stream<Integer> stream) {
            Integer[] coords = stream.toArray(Integer[]::new);
            this.x = coords[0];
            this.y = coords[1];
            this.z = coords[2];
        }

        public double Distance (Coord other) {
            return Math.sqrt(Math.pow(this.x - other.x, 2) +
                             Math.pow(this.y - other.y, 2) +
                             Math.pow(this.z - other.z, 2));
        }
    }

    private class Distance {
        public Coord a;
        public Coord b;
        public double distance;

        public Distance(Coord a, Coord b) {
            this.a = a;
            this.b = b;
            this.distance = a.Distance(b);
        }
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var input = Arrays.stream(inputData.split("\n"))
                .map(x -> new Coord(Arrays.stream(x.split(",")).map(Integer::parseInt)))
                .toArray(Coord[]::new);

        var distances = new ArrayList<Distance>();
        for (int i = 0; i < input.length-1; i++) {
            for (int j = i + 1; j < input.length; j++) {
                distances.add(new Distance(input[i], input[j]));
            }
        }

        distances.sort(Comparator.comparingDouble(a -> a.distance));

        var components = new HashMap<Coord, Integer>();
        var componentId = 0;
        var threshold = part2 ? distances.size() : 1000;
        Distance lastDistance = null;
        for (var i = 0; i < threshold; i++) {
            var distance = distances.get(i);

            var componentA = components.get(distance.a);
            var componentB = components.get(distance.b);
            if (Objects.equals(componentA, componentB) && componentA != null) {
                continue;
            }
            lastDistance = distance;

            if (componentA != null && componentB != null) {
                var min = Math.min(componentA, componentB);
                var max = Math.max(componentA, componentB);
                components.keySet().forEach(coord -> {
                    if (components.get(coord) == max) {
                        components.put(coord, min);
                    }
                });
            }
            else if (componentA != null) {
                components.put(distance.b, componentA);
            } else if (componentB != null) {
                components.put(distance.a, componentB);
            } else {
                components.put(distance.a, componentId);
                components.put(distance.b, componentId);
                componentId++;
            }
            var component_sizes = new int[componentId];
            components.values().forEach(id -> component_sizes[id]++);
            System.out.print("\nIteration " + (i + 1) + ": \n--------------------------------\n");
            System.out.println("Total components: " + Arrays.stream(component_sizes).filter(x -> x > 0).count());
            System.out.println("Distance: " + distance.a.x + " " + distance.b.x + " " + distance.distance);
        }

        if (!part2)
        {
            var component_sizes = new int[componentId];
            components.values().forEach(id -> component_sizes[id]++);
            var sortedSizes = Arrays.stream(component_sizes)
                    .filter(x -> x > 0)
                    .sorted()
                    .toArray();

            System.out.println("Max 3 components multiplied: " + sortedSizes[sortedSizes.length - 1] * sortedSizes[sortedSizes.length - 2] * sortedSizes[sortedSizes.length - 3]);
        } else {
            assert lastDistance != null;
            long multiplication = (long)lastDistance.a.x * (long)lastDistance.b.x;
            System.out.println("The last junction box: " + lastDistance.a.x + " " + lastDistance.b.x + " " + lastDistance.distance);
            System.out.println("Last 2 junction boxes distance to wall: " + multiplication);
        }
    }
}
