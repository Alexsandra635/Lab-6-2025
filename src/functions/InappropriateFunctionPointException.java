package functions;

public class InappropriateFunctionPointException extends Exception {

    public InappropriateFunctionPointException() {
        super("Недопустимое добавление или изменение точки функции.");
    }

    public InappropriateFunctionPointException(String message) {
        super(message);
    }
}