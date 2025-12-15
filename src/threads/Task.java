package threads;
import functions.Function;

public class Task {
    private Function function;
    private double leftBound;
    private double rightBound;
    private double discretizationStep;
    private int taskCount;
    private int currentTaskIndex = -1; // последняя сгенерированная задача

    public int getCurrentTaskIndex() {
        return currentTaskIndex;
    }

    public void incrementTaskIndex() {
        currentTaskIndex++;
    }

    public Task() {}

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public double getLeftBound() { return leftBound; }
    public void setLeftBound(double leftBound) { this.leftBound = leftBound; }

    public double getRightBound() { return rightBound; }
    public void setRightBound(double rightBound) { this.rightBound = rightBound; }

    public double getDiscretizationStep() { return discretizationStep; }
    public void setDiscretizationStep(double step) { this.discretizationStep = step; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

}