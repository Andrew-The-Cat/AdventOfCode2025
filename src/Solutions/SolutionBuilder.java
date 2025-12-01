package Solutions;

import java.util.HashMap;

public class SolutionBuilder {
    private final static HashMap<String, Solution> SolutionRegistry;

    static {
        SolutionRegistry = new HashMap<>();
    }

    public static void registerSolution(String day, Solution solution) {
        SolutionRegistry.put(day, solution);
    }

    public static void execute(String day, String input, boolean part2) throws Exception {
        Solution solution = SolutionRegistry.get(day).setInputData(input);
        if (solution != null) {
            solution.execute(part2);
        } else {
            throw new Exception("No solution registered for day: " + day);
        }
    }
}
