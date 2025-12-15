package functions;
import java.io.Externalizable ;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;

public class LinkedListTabulatedFunction implements TabulatedFunction,Externalizable  {
    private FunctionNode head;
    private int size;
    private static final double EPS = Math.ulp(1.0);

    private static class FunctionNode {
        public FunctionPoint point;
        public FunctionNode prev;
        public FunctionNode next;

        public FunctionNode(FunctionPoint point) {
            this.point = point;
        }
    }
    public LinkedListTabulatedFunction() {
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
    }
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeDouble(getPointX(i));
            out.writeDouble(getPointY(i));
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int n = in.readInt();
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        size = 0;
        for (int i = 0; i < n; i++) {
            FunctionNode node = new FunctionNode(new FunctionPoint());
            node.prev = head.prev;
            node.next = head;
            head.prev.next = node;
            head.prev = node;
            size++;

            node.point.setX(in.readDouble());
            node.point.setY(in.readDouble());
        }
    }
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= size) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        }

        FunctionNode node;
        if (index < size / 2) {
            node = head.next;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = head.prev;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }
        return node;
    }
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode(new FunctionPoint());
        FunctionNode last = head.prev;

        last.next = newNode;
        newNode.prev = last;
        newNode.next = head;
        head.prev = newNode;

        size++;
        return newNode;
    }
    private FunctionNode addNodeByIndex(int index) {
        if (index < 0 || index > size) {
            throw new FunctionPointIndexOutOfBoundsException("Неверный индекс: " + index);
        }

        FunctionNode nextNode = (index == size) ? head : getNodeByIndex(index);
        FunctionNode prevNode = nextNode.prev;

        FunctionNode newNode = new FunctionNode(new FunctionPoint());
        newNode.prev = prevNode;
        newNode.next = nextNode;

        prevNode.next = newNode;
        nextNode.prev = newNode;

        size++;
        return newNode;
    }
    private FunctionNode deleteNodeByIndex(int index) {
        if (size < 3) {
            throw new IllegalStateException("Нельзя удалить точку — останется меньше трёх");
        }

        FunctionNode node = getNodeByIndex(index);
        node.prev.next = node.next;
        node.next.prev = node.prev;

        size--;
        return node;
    }
    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.size = 0;

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            FunctionNode node = addNodeToTail();
            node.point.setX(leftX + i * step);
            node.point.setY(0.0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.size = 0;

        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            FunctionNode node = addNodeToTail();
            node.point.setX(leftX + i * step);
            node.point.setY(values[i]);
        }
    }
    public LinkedListTabulatedFunction(FunctionPoint[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Массив точек не может быть null");
        }
        if (points.length < 2) {
            throw new IllegalArgumentException("Должно быть не менее двух точек");
        }

        this.head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        this.size = 0;

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Точка не может быть null (index = " + i + ")");
            }
            FunctionPoint copy = new FunctionPoint(points[i]);
            if (i > 0) {
                double prevX = head.prev.point.getX();
                double curX = copy.getX();
                if (!(curX > prevX + EPS)) {
                    throw new IllegalArgumentException("Массив точек должен быть упорядочен по X (ошибка на индексе " + i + ")");
                }
            }

            FunctionNode newNode = addNodeToTail();
            newNode.point = copy;
        }
    }

    public FunctionPoint getPoint(int index) {
        FunctionNode node = getNodeByIndex(index);
        return new FunctionPoint(node.point);
    }
    public double getPointX(int index) {
        return getNodeByIndex(index).point.getX();
    }

    public double getPointY(int index) {
        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) {
        getNodeByIndex(index).point.setY(y);
    }
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);
        double x = point.getX();

        // Проверка соседей
        if ((index > 0 && (x < getNodeByIndex(index - 1).point.getX() || Math.abs(x - getNodeByIndex(index - 1).point.getX()) < EPS)) ||
                (index < size - 1 && (x > getNodeByIndex(index + 1).point.getX() || Math.abs(x - getNodeByIndex(index + 1).point.getX()) < EPS))) {
            throw new InappropriateFunctionPointException("Неверная координата X");
        }

        node.point = new FunctionPoint(point);
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        FunctionNode node = getNodeByIndex(index);

        if ((index > 0 && (x < getNodeByIndex(index - 1).point.getX() || Math.abs(x - getNodeByIndex(index - 1).point.getX()) < EPS)) ||
                (index < size - 1 && (x > getNodeByIndex(index + 1).point.getX() || Math.abs(x - getNodeByIndex(index + 1).point.getX()) < EPS))) {
            throw new InappropriateFunctionPointException("Неверное значение X");
        }

        node.point.setX(x);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double x = point.getX();

        for (int i = 0; i < size; i++) {
            if (Math.abs(getNodeByIndex(i).point.getX() - x) < EPS) {
                throw new InappropriateFunctionPointException("Точка с таким X уже существует");
            }
        }

        int insertIndex = 0;
        while (insertIndex < size && getPointX(insertIndex) < x && Math.abs(getPointX(insertIndex) - x) >= EPS) {
            insertIndex++;
        }

        FunctionNode node = addNodeByIndex(insertIndex);
        node.point = new FunctionPoint(point);
    }
    public void deletePoint(int index) {
        if (size <= 2) {
            throw new IllegalStateException("Нельзя удалить точку — останется меньше трёх");
        }
        deleteNodeByIndex(index);
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() - EPS || x > getRightDomainBorder() + EPS)
            return Double.NaN;

        for (int i = 0; i < size - 1; i++) {
            double x0 = getPointX(i);
            double x1 = getPointX(i + 1);
            double y0 = getPointY(i);
            double y1 = getPointY(i + 1);

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

    public double getLeftDomainBorder() {
        return getPointX(0);
    }

    public double getRightDomainBorder() {
        return getPointX(size - 1);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        FunctionNode current = head.next;
        while (current != head) {
            sb.append("(").append(current.point.getX()).append("; ").append(current.point.getY()).append(")");
            if (current.next != head) sb.append(", ");
            current = current.next;
        }
        sb.append("}");
        return sb.toString();
    }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction f = (TabulatedFunction) o;

        if (this.size != f.getPointsCount()) return false;

        // оптимизация LinkedList - LinkedList
        if (o instanceof LinkedListTabulatedFunction) {
            FunctionNode a = this.head.next;
            FunctionNode b = ((LinkedListTabulatedFunction) o).head.next;

            while (a != this.head) {
                if (!a.point.equals(b.point)) return false;
                a = a.next;
                b = b.next;
            }
            return true;
        }

        // сравнение с ArrayTabulatedFunction
        for (int i = 0; i < size; i++) {
            if (!this.getPoint(i).equals(f.getPoint(i))) return false;
        }

        return true;
    }

    public int hashCode() {
        int result = size;
        FunctionNode current = head.next;
        while (current != head) {
            result ^= current.point.hashCode();
            current = current.next;
        }
        return result;
    }
    public Object clone() throws CloneNotSupportedException {
        // создаём массив для клонирования точек
        FunctionPoint[] clonedPoints = new FunctionPoint[this.size]; // используем size
        FunctionNode current = this.head.next;

        for (int i = 0; i < this.size; i++) {
            clonedPoints[i] = (FunctionPoint) current.point.clone(); // глубокое клонирование точки
            current = current.next;
        }

        // создаём новый объект LinkedListTabulatedFunction из массива точек
        return new LinkedListTabulatedFunction(clonedPoints);
    }

}