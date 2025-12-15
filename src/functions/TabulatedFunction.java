package functions;

public interface TabulatedFunction extends Function, Cloneable {
    int getPointsCount();
    double getPointX(int index);
    double getPointY(int index);
    void setPointX(int index, double x) throws InappropriateFunctionPointException;
    void setPointY(int index, double y);
    FunctionPoint getPoint(int index);
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;
    void deletePoint(int index);

    Object clone() throws CloneNotSupportedException;

}