package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private final Random rand = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            synchronized (task) { // синхронизация записи
                double base = 1 + 9 * rand.nextDouble();        // [1,10]
                double left = 100 * rand.nextDouble();         // [0,100]
                double right = left + 100 * rand.nextDouble(); // [left, left+100]
                double step = 0.01 + 0.99 * rand.nextDouble(); // [0.01,1]

                task.setFunction(new Log(base));
                task.setLeftBound(left);
                task.setRightBound(right);
                task.setDiscretizationStep(step);

                System.out.printf("Generated Task %d - Source: %.6f %.6f %.6f%n",
                        i + 1, left, right, step);
            }

            // Небольшая пауза, чтобы дать интегратору шанс прочитать данные
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}