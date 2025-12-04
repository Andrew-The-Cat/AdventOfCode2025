package Solutions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Day3 extends  Solution {
    private void propagate(Character[] batteryJolts, char newJolt, int index) {
        for (int i = batteryJolts.length - 1; i > index; i--) {
            batteryJolts[i] = '0';
        }
        batteryJolts[index] = newJolt;
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var input = inputData.split("\n");
        var maxBatteries = part2 ? 12 : 2;
        long sum = 0;
        for (var line : input) {
            var batteryJolts = new Character[maxBatteries];
            var length = line.length();

            for (int i = 0; i < maxBatteries; i++) {
                batteryJolts[i] = line.charAt(i);
            }

            for (int i = 1; i < length - maxBatteries + 1; i++) {
                for (int j = 0; j < maxBatteries; j++) {
                    System.out.print("Progress: " + (float)(i * j) / (float)(length * maxBatteries) * 100 + "%\r");

                    if (line.charAt(i + j) > batteryJolts[j]) {
                        propagate(batteryJolts, line.charAt(i + j), j);
                    }
                }
            }

            for (int i = 1; i < maxBatteries; i++) {
                char c = line.charAt(length - maxBatteries + i);
                if (batteryJolts[i] < c) {
                    batteryJolts[i] = c;
                    propagate(batteryJolts, c, i);
                }
            }

            var str = Arrays.stream(batteryJolts).map(Object::toString).collect(Collectors.joining(""));

            //System.out.println(STR."Highest joltage: \{str}");
            sum += Long.parseLong(str);
        }

        System.out.println("Total sum of jolts: " + sum);
    }
}
