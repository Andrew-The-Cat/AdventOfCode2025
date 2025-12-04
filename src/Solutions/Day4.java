package Solutions;

import java.util.ArrayList;

public class Day4 extends Solution {
    private void printList(ArrayList<ArrayList<Character>> list) {
        for (var row : list) {
            for (var c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private int countNeighbors(ArrayList<ArrayList<Character>> list, int x, int y, char target) {
        int count = 0;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue; // Skip the cell itself
                int newX = x + dx;
                int newY = y + dy;

                if (newX >= 0 && newX < list.size() && newY >= 0 && newY < list.getFirst().size()) {
                    if (list.get(newX).get(newY) == target) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private void copyList(ArrayList<ArrayList<Character>> source, ArrayList<ArrayList<Character>> destination) {
        for (int i = 0; i < source.size(); i++) {
            for (int j = 0; j < source.getFirst().size(); j++) {
                if (source.get(i).get(j) == 'x')
                    destination.get(i).set(j, '.');
                destination.get(i).set(j, source.get(i).get(j));
            }
        }
    }

    private int parseList(ArrayList<ArrayList<Character>> list, ArrayList<ArrayList<Character>> outputList) {
        var count = 0;
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.getFirst().size(); j++) {
                System.out.print("Progress: " +(float) (i * j) / (float) (list.size() * list.getFirst().size()) * 100 + "\r");
                if (list.get(i).get(j) != '@')
                    continue;
                if (countNeighbors(list, i, j, '@') < 4) {
                    count++;
                    outputList.get(i).set(j, 'x');
                }
            }
        }

        return count;
    }

    @Override
    public void execute(boolean part2) throws Exception {
        ArrayList<ArrayList<Character>> list = new ArrayList<>();
        ArrayList<ArrayList<Character>> displayList = new ArrayList<>();
        var input = inputData.split("\n");

        for (var line : input) {
            list.add(new ArrayList<>());
            displayList.add(new ArrayList<>());
            for (var c : line.toCharArray()) {
                list.getLast().add(c);
                displayList.getLast().add(c);
            }
        }

        int count = parseList(list, displayList);
        int sum = count;

        if (part2) {
            int safeCount = 0;
            while (count > 0 && safeCount < 1000) {
                safeCount++;
                copyList(displayList, list);
                count = parseList(list, displayList);
                sum += count;
            }
        }

        printList(displayList);
        System.out.println("Number of movable paper rolls: " + sum);
    }
}
