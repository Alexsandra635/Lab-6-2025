package functions.meta;

import functions.Function;

public class Power implements Function {

    private Function baseFunction;
    private double power;

    public Power(Function baseFunction, double power) {
        this.baseFunction = baseFunction;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return baseFunction.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return baseFunction.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return Math.pow(baseFunction.getFunctionValue(x), power);
    }
}