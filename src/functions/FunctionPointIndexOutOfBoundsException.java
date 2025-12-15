package functions;


public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {

    public FunctionPointIndexOutOfBoundsException() {
        super("Индекс точки функции вне допустимого диапазона.");
    }

    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }
}