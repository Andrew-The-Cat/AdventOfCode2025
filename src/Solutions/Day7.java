package Solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.Math.max;

public class Day7 extends Solution {
    private static class TacBeam {
        private final boolean part2;
        private final int x;
        private int y;
        private void Create(int x, int y, HashMap<TacBeam, Long> tacBeams, long NumParents) {
            var t = new TacBeam(x, y, part2);
            for (var tacBeam : tacBeams.keySet()) {
                if (tacBeam.x == x && tacBeam.y == y) {
                    if (part2) {
                        tacBeams.put(tacBeam, tacBeams.get(tacBeam) + NumParents);
                    }
                    return;
                }
            }
            tacBeams.put(t, NumParents);
        }
        public TacBeam(int x, int y, boolean part2) {
            this.part2 = part2;
            this.x = x;
            this.y = y;
        }

        public long stepSimulation(char[][] grid, HashMap<TacBeam, Long> tacBeams) {
            if (this.y >= grid.length - 1 || this.x >= grid[0].length || this.x < 0) {
                tacBeams.remove(this);
                return 0;
            }
            long cnt = 0;
            this.y += 1;
            if (grid[this.y][this.x] == '^') {
                if (!this.part2) {
                    cnt++;
                } else {
                    cnt += 2 * tacBeams.get(this);
                }
                this.Create(this.x + 1, this.y, tacBeams, tacBeams.get(this));
                this.Create(this.x - 1, this.y, tacBeams, tacBeams.get(this));
                tacBeams.remove(this);
            }
            return cnt;
        }
    }

    private String StrPrintGrid (char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (var row : grid) {
            for (var cell : row) {
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private long NumActiveBeams (HashMap<TacBeam, Long> tacBeams) {
        long cnt = 0;
        for (var val : tacBeams.values()) {
            cnt += val;
        }
        return cnt;
    }

    @Override
    public void execute(boolean part2) throws Exception {
        var input = Arrays.stream(inputData.split("\n"))
                .map(x -> x.replace(" ", "").toCharArray()).toArray(char[][]::new);

        HashMap<TacBeam, Long> tacBeams = new HashMap<>();

        for (var row = 0; row < input.length; row++) {
            for (var col = 0; col < input[row].length; col++) {
                if (input[row][col] == 'S') {
                    tacBeams.put(new TacBeam(col, row, part2), 1L);
                }
            }
        }

        long splits = 0;
        var safetyLimit = 10000;
        while (!tacBeams.isEmpty() && safetyLimit-- > 0) {
            var tacBeamsCopy = new ArrayList<>(tacBeams.keySet());
            for (var tacBeam : tacBeamsCopy) {
                if (input[tacBeam.y][tacBeam.x] == '|' && !part2) {
                    tacBeams.remove(tacBeam);
                    continue; // Already processed this beam this step
                }
                input[tacBeam.y][tacBeam.x] = '|';
                var tmp = tacBeam.stepSimulation(input, tacBeams);
                if (!part2)
                    splits += tmp;
            }
            if (part2) {
                splits = max(NumActiveBeams(tacBeams), splits);
            }


            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.printf("""
                    Beams active: %d
                    Splits so far: %d
                    -----------------------------
                    %s
                    %n""", NumActiveBeams(tacBeams), splits, StrPrintGrid(input));
            Thread.sleep(10);
            //System.in.read();
        }

        System.out.println("Total splits: " + splits);
    }
}
