package Solutions;

import java.util.ArrayList;
import java.util.Arrays;

public class Day6 extends Solution {
    @Override
    public void execute(boolean part2) throws Exception {
        if (!part2) {
            var input = Arrays.stream(inputData.split("\n"))//break input into lines
                    .map(x -> Arrays.stream(x.split(" ")).filter(y -> !y.isEmpty()).toArray(String[]::new))
                    .toArray(String[][]::new);

            long grandSum = 0;
            for (int col = 0; col < input[0].length; col++) {
                long sum = 0;
                long product = 1;
                for (int row = 0; row < input.length - 1; row++) {
                    sum += Long.parseLong(input[row][col]);
                    product *= Long.parseLong(input[row][col]);
                }

                if (input[input.length - 1][col].equals("+")) {
                    grandSum += sum;
                } else if (input[input.length - 1][col].equals("*")) {
                    grandSum += product;
                } else {
                    throw new Exception("Invalid operation: " + input[input.length - 1][col]);
                }
            }
            System.out.println("Grand total: " + grandSum);
        }
        else {
            var input = Arrays.stream(inputData.split("\n"))
                    .map(String::toCharArray).toArray(char[][]::new);

            long grandSum = 0;
            char operation = '+';
            boolean gotOperation = false;
            ArrayList<Long> operands = new ArrayList<>();
            for (int col = 0; col < input[0].length; col++) {
                long sum = 0;
                long product = 1;
                boolean found = false;
                if (!gotOperation && input[input.length - 1][col] != ' ') {
                    operation = input[input.length - 1][col];
                    gotOperation = true;
                }
                long val = 0;
                for (int row = 0; row < input.length - 1; row++) {
                    if (input[row][col] != ' ') {
                        found = true;
                        val = val * 10 + Character.getNumericValue(input[row][col]);
                    }
                }
                if (found) {
                    operands.add(val);
                }
                if (!found || col == input[0].length - 1) {
                    gotOperation = false;
                    for (var x : operands) {
                        sum += x;
                        product *= x;
                    }

                    operands.clear();
                    if (operation == '+') {
                        grandSum += sum;
                    } else if (operation == '*') {
                        grandSum += product;
                    } else {
                        throw new Exception("Invalid operation: " + operation);
                    }
                }
            }
            System.out.println("Grand total: " + grandSum);
        }
    }
}
