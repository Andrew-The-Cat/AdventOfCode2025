package Solutions;

import java.util.*;

public class Day11 extends Solution {
    private long countPaths(Map<String, ArrayList<String>> map, String start, String end) {
        var cache = new HashMap<String, Long>();
        var visiting = new HashSet<String>();
        return dfsCount(start, end, map, cache, visiting);
    }

    private long dfsCount(String node, String end, Map<String, ArrayList<String>> map,
                          Map<String, Long> cache, Set<String> visiting) {
        if (node.equals(end))
            return 1L;
        if (cache.containsKey(node))
            return cache.get(node);
        if (visiting.contains(node)) {
            System.err.println("There be cycles in this 'ere graph");
            return 0L;
        }

        visiting.add(node);
        long sum = 0L;
        for (var neighbour : map.getOrDefault(node, new ArrayList<>())) {
            sum += dfsCount(neighbour, end, map, cache, visiting);
        }
        visiting.remove(node);
        cache.put(node, sum);
        return sum;
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var map = new HashMap<String, ArrayList<String>>();
        Arrays.stream(inputData.split("\n"))
                .forEach(x -> {
                    var parts = x.split(":");
                    map.putIfAbsent(parts[0], new ArrayList<>());
                    map.put(parts[0], new ArrayList<>(Arrays.asList(parts[1].trim().split(" "))));
                });

        if (!part2) {
            System.out.println("Total Paths: " + countPaths(map, "you", "out"));
        } else {
            System.out.println("Total Paths: " + countPaths(map, "svr", "out"));

            long numPathsOne = countPaths(map, "dac", "out") * countPaths(map, "fft", "dac") * countPaths(map, "svr", "fft");
            long numPathsTwo = countPaths(map, "fft", "out") * countPaths(map, "dac", "fft") * countPaths(map, "svr", "dac");
            System.out.println("Total Paths with detour: " + (numPathsOne + numPathsTwo));
        }
    }
}
