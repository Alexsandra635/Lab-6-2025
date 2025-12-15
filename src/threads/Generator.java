package threads;
import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < task.getTaskCount(); i++) {
            // Генерация всех данных заранее
            double base = 1 + 9 * rand.nextDouble();
            double left = 100 * rand.nextDouble();
            double right = left + 100 * rand.nextDouble();
            double step = 0.01 + 0.99 * rand.nextDouble();
            Log func = new Log(base); // создание объекта функции вынесено

            try {
                // Короткий блок семафора — только присвоение
                semaphore.beginWrite();
                task.setFunction(func);      // присвоение внутри семафора
                task.setLeftBound(left);
                task.setRightBound(right);
                task.setDiscretizationStep(step);
                task.incrementTaskIndex(); // помечаем задачу как готовую
                semaphore.endWrite();

                System.out.printf("Generated Task %d - Source: %.6f %.6f %.6f%n", i + 1, left, right, step);

                Thread.sleep(5);

            } catch (InterruptedException e) {
                System.out.println("Generator прерван.");
                break;
            }
        }
    }
}