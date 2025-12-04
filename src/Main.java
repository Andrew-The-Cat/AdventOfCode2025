import Solutions.*;

import java.util.Scanner;

void main() {
    System.out.println("Hello, World!");
    System.out.println("Starting AOC 2025 interactive console...");

    try {
        SolutionBuilder.registerSolution("day1", new Day1());
        SolutionBuilder.registerSolution("day2", new Day2());
        SolutionBuilder.registerSolution("day3", new Day3());
        SolutionBuilder.registerSolution("day4", new Day4());
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }

    Scanner input = new Scanner(System.in);
    while (true) {
        try {
            System.out.print("Enter day<x> (or 'exit' to quit) where x is the day number: ");
            String day = input.nextLine();
            if (day.equalsIgnoreCase("exit")) {
                break;
            }

            while (!day.matches("day\\d+")) {
                System.out.print("Invalid day format. Please enter again (or 'exit' to quit): ");
                day = input.nextLine();
                if (day.equalsIgnoreCase("exit")) {
                    return;
                }
            }

            System.out.print("Would you like to receive the answer for part 2: ");
            String part2Input = input.nextLine();
            boolean part2 = part2Input.equalsIgnoreCase("yes") || part2Input.equalsIgnoreCase("y");

            boolean running = true;
            while (true) {
                System.out.println("\nAwaiting input data (end with an empty line or type 'exit' to return to day selection):");
                StringBuilder inputDataBuilder = new StringBuilder();
                String line;
                while (!(line = input.nextLine()).isEmpty()) {
                    if (line.equalsIgnoreCase("exit")) {
                        running = false;
                        break;
                    }
                    inputDataBuilder.append(line).append("\n");
                }
                if (!running) {
                    break;
                }
                String inputData = inputDataBuilder.toString().trim();

                SolutionBuilder.execute(day, inputData, part2);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}