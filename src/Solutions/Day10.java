package Solutions;

import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.type.context.NumberContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;

public class Day10 extends Solution {
    // Taken from copilot :3
    // Ngl I rly wasn't feeling like doing the verification myself cause AOC is complaining my solution is... tooo loowwww
    // And in the end it was the mighty off by one rounding error that got me :D
    private boolean verifySolution(
            ArrayList<Integer[]> buttons,
            int[] joltages,
            BigDecimal[] solutionX
    ) {
        int m = joltages.length;
        int n = buttons.size();

        // 1. Check each variable is an integer ≥ 0
        long[] longX = new long[n];
        for (int i = 0; i < n; i++) {
            BigDecimal val = solutionX[i];

            BigDecimal rounded = val.setScale(0, RoundingMode.HALF_UP);
            if (val.subtract(rounded).abs().doubleValue() > 1e-6) {
                System.out.println("Variable x[" + i + "] = " + val + " is NOT integer-ish!");
                return false;
            }

            longX[i] =rounded.longValue();

            if (longX[i] < 0) {
                System.out.println("Variable x[" + i + "] = " + longX[i] +
                        " is negative!");
                return false;
            }
        }

        // 2. Check constraint satisfaction
        for (int j = 0; j < m; j++) {
            long sum = 0;

            for (int i = 0; i < n; i++) {
                Integer[] affected = buttons.get(i);
                for (int a : affected) {
                    if (a == j) {
                        sum += longX[i];
                        break; // only count once per button
                    }
                }
            }

            if (sum != joltages[j]) {
                System.out.println("Constraint failed at joltage " + j +
                        ": expected " + joltages[j] +
                        ", got "   + sum);
                return false;
            }
        }

        System.out.println("Solution verified!");
        return true;
    }

    private int readLights(String command) {
        // Consume the brackets
        command = command.substring(1, command.length() - 1);

        int lights = 0;
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == '#') {
                lights |= (1 << i);
            }
        }
        return lights;
    }

    private Integer[] readButton(String command) {
        // Consume the brackets
        command = command.substring(1, command.length() - 1);

        return Arrays.stream(command.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
    }

    private int[] readJolts(String command) {
        // Consume the brackets
        command = command.substring(1, command.length() - 1);

        return Arrays.stream(command.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    private int minimumPresses = Integer.MAX_VALUE;

    private int pressButton(int lights, Integer[] button) {
        int buttonMask = 0;
        for (Integer integer : button) {
            buttonMask |= (1 << integer);
        }

        // Toggle the lights
        lights ^= buttonMask;

        // Push the new state onto the stack
        return lights;
    }

    private void PartOne(int lights, ArrayList<Integer[]> buttons, int presses) {
        if (lights == 0) {
            minimumPresses = Math.min(minimumPresses, presses);
            return;
        }
        if (buttons.isEmpty() || presses >= minimumPresses) {
            return;
        }

        while(!buttons.isEmpty()) {
            var button = buttons.getFirst();
            int newLights = pressButton(lights, button);

            var remainingButtons = new ArrayList<>(buttons);
            remainingButtons.remove(button);

            PartOne(newLights, remainingButtons, presses + 1);

            buttons.removeFirst();
        }
    }

    // This was done in part with the help of Copilot cause I didn't even know this library existed :3
    private void PartTwo(int[] joltages, ArrayList<Integer[]> buttons) {
        ExpressionsBasedModel model = new ExpressionsBasedModel();

        // The number of times each button is pressed
        Variable[] counters = new Variable[buttons.size()];
        for (int i = 0; i < buttons.size(); i++) {
            counters[i] = model.addVariable("button_" + i)
                    .lower(0) // The button can be pressed zero or more times --- equivalent to being bounded below by 0
                    .integer(true); // The button presses must be an integer value
        }

        for (int c = 0; c < joltages.length; c++) { // For each joltage counter
            Expression eq = model.addExpression("eq_" + c).level(joltages[c]); // The equation must equal the target joltage

            // Where
            for (int b = 0; b < buttons.size(); b++) { // For each button
                var affected = buttons.get(b); // Get the joltage counters affected by this button

                for (int a : affected) { // And for each affected counter
                    if (a == c) {
                        // Where counters is the array of button press variables from before
                        eq.set(counters[b], 1); // Let the model know that pressing this button affects said counter
                        break;
                    }
                }
            }
        }
        /// In general, to give a synopsis for future me (hai future me :D):
        /// We are building a set of linear equations to solve the problem.
        /// Each button press variable contributes to certain joltage counters.
        /// The goal is to find the minimum number of button presses such that all joltage counters
        /// reach their target values as specified in the joltages array.

        /// The declared variables function like a variable in a system of equations (think x0 + x1 + 2*x2 = 5).
        /// The expressions function like the equations themselves.
        /// Finally, all the is left to do is to set the objective function to minimize the sum of all button presses.

        // The task the model is going to solve: Minimize the total number of button presses
        // PS. if there were multiple objectives, weight would determine which would be more important
        Expression objective = model.addExpression("MinimizePresses").weight(1.0);
        for (var j = 0; j < buttons.size(); j++) {
            objective.set(counters[j], 1); // Each button press adds 1 to the total count of presses
        }

        var result = model.minimise();

        var sol = result.getSolution(NumberContext.ofPrecision(16));
        BigDecimal[] solutionX = new BigDecimal[sol.size()];
        for (int i = 0; i < sol.size(); i++) {
            solutionX[i] = sol.get(i);
        }

        if (result.getState().isFeasible() && verifySolution(buttons, joltages, solutionX)) {
            minimumPresses = (int)Math.round(result.getValue()); // Off by freakin one
        } else {
            minimumPresses = -1; // Indicate no solution found
        }
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var input = inputData.split("\n");
        int count = 0;
        var sum = 0;

        // Js for the fuck of it I'm using bitmasks to represent machine lights
        for (var line : input) {
            minimumPresses = Integer.MAX_VALUE;
            count++;
            var groups = line.split(" ");

            var lightStr = new StringBuilder(groups[0]).reverse().toString();
            int lights = readLights(lightStr);
            var buttons = new ArrayList<Integer[]>();
            var joltages = readJolts(groups[groups.length-1]);

            for (var i = 1; i < groups.length -1; i++) {
                buttons.add(readButton(groups[i]));
            }
            if (!part2)
                PartOne(lights, buttons, 0);
            else
                PartTwo(joltages, buttons);

            sum += minimumPresses;
            System.out.println("Minimum presses found for machine " + count + " is: " + minimumPresses);
        }

        System.out.println("Minimum presses found for all machines is: " + sum);
    }
}
