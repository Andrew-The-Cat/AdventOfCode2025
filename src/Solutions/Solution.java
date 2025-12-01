package Solutions;

public abstract class Solution {
    protected String inputData;

    public Solution setInputData(String inputData) {
        this.inputData = inputData;
        return this;
    }

    public abstract void execute(boolean part2) throws Exception;
}
