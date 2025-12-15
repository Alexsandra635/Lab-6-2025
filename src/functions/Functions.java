package functions;

import functions.meta.*;

public class Functions {

    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
    public static double integrate(Function f, double leftX, double rightX, double step) {
        if (leftX < f.getLeftDomainBorder() || rightX > f.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы функции");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("Шаг интегрирования должен быть положительным");
        }

        double sum = 0.0;
        double x = leftX;

        while (x < rightX) {
            double nextX = Math.min(x + step, rightX); // учёт последнего участка, если меньше шага
            double y1 = f.getFunctionValue(x);
            double y2 = f.getFunctionValue(nextX);
            sum += (y1 + y2) / 2 * (nextX - x); // площадь трапеции
            x = nextX;
        }

        return sum;
    }


}