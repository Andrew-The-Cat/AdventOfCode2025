package Solutions;


import java.util.HashSet;
import java.util.stream.*;
import static java.util.Arrays.*;

public class Day2 extends Solution {
    @Override
    public void execute(boolean part2) {
        inputData = inputData.replaceAll("\n", "");
        var input = stream(inputData.split(","))
                .map(interval -> stream(interval.split("-")).mapToLong(Long::parseLong))
                .toArray(LongStream[]::new);

        long sum = 0;
        for (var in : input) {
            var inArr = in.toArray();
            // Transform the input range into the cut version (Eg. 1000-9999 -> 10-99)
            String[] aux = new String[]{Long.toString(inArr[0]), Long.toString(inArr[1])};
            var i_start = 2;
            var i_end = aux[1].length();

            if (!part2) {
                i_end = 2;
            }

            var repeats = new HashSet<Long>();

            // Since part 2 requires checking sequences of different lengths, loop through all possible lengths
            for (var i = i_start; i <= i_end; i++) {
                var c_range = new long[]{
                        aux[0].length() / i > 0 ? Long.parseLong(aux[0].substring(0, aux[0].length() / i)) : 0,
                        Long.parseLong(aux[1].substring(0, aux[1].length() / i + 1))
                };

                // Generate all "reconstructed" numbers in the cut range and sum those that fit in the original range
                var c_start = c_range[0];
                var c_end = c_range[1];

                for (long j = c_start; j <= c_end; j++) {
                    long finalJ = j;
                    long reconstructed = Long.parseLong(
                            IntStream.range(0, i)
                                    .mapToObj(x -> Long.toString(finalJ))
                                    .collect(Collectors.joining())
                    );
                    if (reconstructed > inArr[1]) break;
                    if (reconstructed < inArr[0]) continue;
                    if (repeats.contains(reconstructed)) continue;
                    repeats.add(reconstructed);
                    sum += reconstructed;

                    //System.out.println("Adding number: " + reconstructed);
                }
            }

        }
        System.out.println("Total sum of all numbers in the intervals: " + sum);
    }
}
