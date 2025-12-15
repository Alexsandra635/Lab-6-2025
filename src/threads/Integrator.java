package threads;
import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTaskCount(); i++) {
            boolean done = false;

            while (!done) {
                try {
                    semaphore.beginRead();

                    // Проверяем, подготовил ли генератор i-ю задачу
                    if (task.getCurrentTaskIndex() >= i) {
                        double left = task.getLeftBound();
                        double right = task.getRightBound();
                        double step = task.getDiscretizationStep();

                        double result = Functions.integrate(task.getFunction(), left, right, step);

                        System.out.printf("Integrator Task %d - Result: %.6f %.6f %.6f %.6f%n",
                                i + 1, left, right, step, result);

                        done = true; // интегрирование выполнено
                    }

                    semaphore.endRead();

                    if (!done) {
                        Thread.sleep(1); // ждём генератора
                    }

                } catch (InterruptedException e) {
                    System.out.println("Integrator прерван.");
                    done = true;
                    break;
                }
            }
        }

    }
}