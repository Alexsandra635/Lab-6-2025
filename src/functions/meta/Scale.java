package functions.meta;

import functions.Function;

public class Scale implements Function {

    private Function f;
    private double kx;
    private double ky;

    public Scale(Function f, double kx, double ky) {
        this.f = f;
        this.kx = kx;
        this.ky = ky;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() * kx;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() * kx;
    }

    public double getFunctionValue(double x) {
        return ky * f.getFunctionValue(x / kx);
    }
}