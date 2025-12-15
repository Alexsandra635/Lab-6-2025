package threads;

import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            double result;
            double left, right, step;

            synchronized (task) { // синхронизация чтения
                left = task.getLeftBound();
                right = task.getRightBound();
                step = task.getDiscretizationStep();
                result = Functions.integrate(task.getFunction(), left, right, step);
            }

            System.out.printf("Integrated Task %d - Result: %.6f %.6f %.6f %.6f%n",
                    i + 1, left, right, step, result);

            // Небольшая пауза
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}