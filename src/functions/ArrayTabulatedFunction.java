package functions;
import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction,Serializable {
    private FunctionPoint[] points;
    private int size;
    private static final double EPS = Math.ulp(1.0);

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.points = new FunctionPoint[pointsCount];
        this.size = pointsCount;
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }
        int n = values.length;
        this.points = new FunctionPoint[n];
        this.size = n;
        double step = (rightX - leftX) / (n - 1);
        for (int i = 0; i < n; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Должно быть не менее двух точек");
        }

        int n = points.length;
        this.points = new FunctionPoint[n];
        this.size = n;

        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Точка не может быть null (index = " + i + ")");
            }

            this.points[i] = new FunctionPoint(points[i]);
            if (i > 0) {
                double prevX = this.points[i - 1].getX();
                double curX = this.points[i].getX();
                if (!(curX > prevX + EPS)) {
                    throw new IllegalArgumentException("Массив точек должен быть упорядочен по X (ошибка на индексе " + i + ")");
                }
            }
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс " + index + " вне диапазона точек");
        }
    }

    public double getLeftDomainBorder() {
        return points[0].getX();
    }

    public double getRightDomainBorder() {
        return points[size - 1].getX();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - EPS || x > getRightDomainBorder() + EPS)
            return Double.NaN;

        for (int i = 0; i < size - 1; i++) {
            double x0 = points[i].getX();
            double x1 = points[i + 1].getX();
            double y0 = points[i].getY();
            double y1 = points[i + 1].getY();

            if (Math.abs(x - x0) < EPS) return y0;
            if (Math.abs(x - x1) < EPS) return y1;

            if (x > x0 && x < x1) {
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return Double.NaN;
    }

    public int getPointsCount() {
        return size;
    }

    public FunctionPoint getPoint(int index) {
        checkIndex(index);
        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        checkIndex(index);
        double x = point.getX();


        if ((index > 0 && (x < points[index - 1].getX() || Math.abs(x - points[index - 1].getX()) < EPS)) ||
                (index < size - 1 && (x > points[index + 1].getX() || Math.abs(x - points[index + 1].getX()) < EPS))) {
            throw new InappropriateFunctionPointException("Неверная координата X для данной позиции");
        }
        points[index] = new FunctionPoint(point);
    }
    public double getPointX(int index) {
        checkIndex(index);
        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        checkIndex(index);

        if ((index > 0 && (x < points[index - 1].getX() || Math.abs(x - points[index - 1].getX()) < EPS)) ||
                (index < size - 1 && (x > points[index + 1].getX() || Math.abs(x - points[index + 1].getX()) < EPS))) {
            throw new InappropriateFunctionPointException("Неверное значение X — нарушается порядок точек");
        }

        points[index].setX(x);
    }
    public double getPointY(int index) {
        checkIndex(index);
        return points[index].getY();
    }
    public void setPointY(int index, double y) {
        checkIndex(index);
        points[index].setY(y);
    }

    public void deletePoint(int index) {
        checkIndex(index);
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку — останется меньше трёх");
        }
        for (int i = index; i < size - 1; i++) {
            points[i] = points[i + 1];
        }
        size--;
    }
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();
        for (int i = 0; i < size; i++) {
            if (Math.abs(points[i].getX() - x) < EPS) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }
        if (size == points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[size * 2];
            System.arraycopy(points, 0, newPoints, 0, size);
            points = newPoints;
        }

        int insertIndex = 0;
        while (insertIndex < size && points[insertIndex].getX() < x && Math.abs(points[insertIndex].getX() - x) >= EPS) {
            insertIndex++;
        }

        for (int i = size; i > insertIndex; i--) {
            points[i] = points[i - 1];
        }

        points[insertIndex] = new FunctionPoint(point);
        size++;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < size; i++) {
            sb.append("(").append(points[i].getX()).append("; ").append(points[i].getY()).append(")");
            if (i != size - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction f = (TabulatedFunction) o;

        if (this.size != f.getPointsCount()) return false;

        // оптимизация Array - Array
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction af = (ArrayTabulatedFunction) o;
            for (int i = 0; i < size; i++) {
                if (!this.points[i].equals(af.points[i])) return false;
            }
            return true;
        }

        // сравнение с LinkedList
        for (int i = 0; i < size; i++) {
            if (!this.points[i].equals(f.getPoint(i))) return false;
        }
        return true;
    }


    public int hashCode() {
        int hash = size;
        for (int i = 0; i < size; i++) {
            hash ^= points[i].hashCode();
        }
        return hash;
    }

    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] newPoints = new FunctionPoint[size];
        for (int i = 0; i < size; i++) {
            newPoints[i] = (FunctionPoint) points[i].clone(); // глубокое клонирование
        }
        return new ArrayTabulatedFunction(newPoints);
    }


}