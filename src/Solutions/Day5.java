package Solutions;

import java.util.Arrays;
import java.util.Comparator;

public class Day5 extends Solution {
    private void reduce (java.util.ArrayList<long[]> stack) {
        for (int i = 0; i < stack.size() - 1; i++) {
            var current = stack.get(i);
            var next = stack.get(i + 1);
            if (current[1] >= next[0] - 1) {
                current[1] = Math.max(current[1], next[1]);
                stack.remove(i + 1);
                i--;
            }
        }
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var splitIn = inputData.split("\n\n");
        var rangesIn = new java.util.ArrayList<>(Arrays.stream(splitIn[0].split("\n"))
                .map(x -> {
                    var nums = Arrays.stream(x.split("-")).mapToLong(Long::parseLong).toArray();
                    return new long[]{nums[0], nums[1]};
                }).toList());
        var idsIn = Arrays.stream(splitIn[1].split("\n")).map(Long::parseLong).toList();

        long count = 0;
        if (!part2) {
            for (var id : idsIn) {
                boolean contained = false;
                for (var range : rangesIn) {
                    if (id >= range[0] && id <= range[1]) {
                        contained = true;
                        break;
                    }
                }
                if (contained) {
                    count++;
                }
            }

            System.out.println("Total fresh produce: " + count);
        } else {
            rangesIn.sort(Comparator.comparingLong(x -> x[0]));
            var stack = new java.util.ArrayList<long[]>();
            for (var range : rangesIn) {
                stack.add(new long[]{range[0], range[1]});
                reduce(stack);
            }

            for (var range : stack) {
                count += (range[1] - range[0] + 1);
            }
            System.out.println("Total possible produce IDs: " + count);
        }
    }
}
