package functions.meta;

import functions.Function;

public class Composition implements Function {

    private Function outer;
    private Function inner;

    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        return outer.getFunctionValue(inner.getFunctionValue(x));
    }
}