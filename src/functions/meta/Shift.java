package functions.meta;

import functions.Function;

public class Shift implements Function {

    private Function f;
    private double dx;
    private double dy;

    public Shift(Function f, double dx, double dy) {
        this.f = f;
        this.dx = dx;
        this.dy = dy;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + dx;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + dx;
    }

    public double getFunctionValue(double x) {
        return f.getFunctionValue(x - dx) + dy;
    }
}