package Solutions;

public class Day1 extends Solution {
    @Override
    public void execute(boolean part2) throws Exception {
        String[] lines = inputData.split("\n");
        int sum = 50;
        int prevSum = sum;
        int numZeroesP1 = 0;
        int numZeroesP2 = 0;

        for (String line : lines) {
            char direction = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));

            switch (direction) {
                case 'L':
                    sum -= value;
                    break;
                case 'R':
                    sum += value;
                    break;
                default:
                    throw new Exception("Invalid direction: " + direction);
            }

            // Check how many times the sum is out of bounds
            numZeroesP2 += Math.abs(sum / 100) + (sum > 0 || (prevSum == 0 && sum < 0) ? 0 : 1);

            sum = ((sum % 100) + 100) % 100; // Keep sum within 0-99

            //System.out.println("Dial now points at: " + sum);
            if (sum == 0) {
                numZeroesP1++;
            }
            prevSum = sum;
        }

        if (part2) {
            System.out.println("Number of times dial points at 0: " + numZeroesP2);
        } else {
            System.out.println("Number of times dial points at 0: " + numZeroesP1);
        }
    }
}
