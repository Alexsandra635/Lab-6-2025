package functions;
import java.io.*;
import java.util.*;
import java.io.StreamTokenizer;
public class TabulatedFunctions {
    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше двух");
        }

        double step = (rightX - leftX) / (pointsCount - 1);
        double[] yValues = new double[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            yValues[i] = function.getFunctionValue(x);
        }

        return new ArrayTabulatedFunction(leftX, rightX, yValues);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        int pointsCount = function.getPointsCount();
        dos.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++) {
            dos.writeDouble(function.getPointX(i));
            dos.writeDouble(function.getPointY(i));
        }
        dos.flush();
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        int pointsCount = dis.readInt();
        double[] yValues = new double[pointsCount];
        double leftX = dis.readDouble();
        double rightX = leftX; // будем пересчитывать rightX
        yValues[0] = dis.readDouble();
        for (int i = 1; i < pointsCount; i++) {
            double x = dis.readDouble();
            yValues[i] = dis.readDouble();
            rightX = x;
        }
        return new ArrayTabulatedFunction(leftX, rightX, yValues);
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException {
        BufferedWriter bw = new BufferedWriter(out);
        int pointsCount = function.getPointsCount();
        bw.write(String.valueOf(pointsCount));
        bw.write(" ");
        for (int i = 0; i < pointsCount; i++) {
            bw.write(function.getPointX(i) + " " + function.getPointY(i) + " ");
        }
        bw.newLine();
        bw.flush();
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer st = new StreamTokenizer(in);
        st.nextToken();
        int pointsCount = (int) st.nval;
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];
        for (int i = 0; i < pointsCount; i++) {
            st.nextToken();
            xValues[i] = st.nval;
            st.nextToken();
            yValues[i] = st.nval;
        }
        return new ArrayTabulatedFunction(xValues[0], xValues[pointsCount - 1], yValues);
    }

}
