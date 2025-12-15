import functions.Functions;
import functions.basic.Exp;
import functions.basic.Log;
import threads.*;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // Задание 1: Проверка интеграла экспоненты
        System.out.println("=== Задание 1: Интеграл exp(x) от 0 до 1 ===");
        double leftExp = 0.0;
        double rightExp = 1.0;

        double stepExp = 0.1;
        double theoretical = Math.exp(1) - 1;
        double resultExp;
        do {
            resultExp = Functions.integrate(new Exp(), leftExp, rightExp, stepExp);
            stepExp /= 2;
        } while (Math.abs(resultExp - theoretical) > 1e-7);

        System.out.printf("Интеграл exp(x) от %.2f до %.2f с шагом %.10f = %.10f%n",
                leftExp, rightExp, stepExp, resultExp);

        // === Задание 2: Последовательное выполнение 100 логарифмических интегралов ===
        System.out.println("\n Задание 2: 100 случайных логарифмических интегралов ");
        nonThread();

        // === Задание 3: Потоковое выполнение ===
        System.out.println("\n Задание 3: Потоковая генерация и интегрирование ");
        simpleThreads();

        // === Задание 4: Потоковое выполнение с семафором и прерыванием ===
        System.out.println("\n Задание 4: Потоки с семафором и прерыванием ");
        complicatedThreads();
    }

    // Метод последовательного выполнения (Задание 2)
    public static void nonThread() {
        Random rand = new Random();
        Task task = new Task();
        task.setTaskCount(100);

        System.out.println("Количество заданий: " + task.getTaskCount());

        for (int i = 0; i < task.getTaskCount(); i++) {
            int taskNumber = i + 1;

            double base = 1 + 9 * rand.nextDouble();
            double left = 100 * rand.nextDouble();
            double right = left + 100 * rand.nextDouble();
            double step = 0.01 + 0.99 * rand.nextDouble();

            task.setFunction(new Log(base));
            task.setLeftBound(left);
            task.setRightBound(right);
            task.setDiscretizationStep(step);

            System.out.printf("Task %d - Source: %.6f %.6f %.6f%n", taskNumber, left, right, step);

            try {
                double result = Functions.integrate(task.getFunction(), left, right, step);
                System.out.printf("Task %d - Result: %.6f %.6f %.6f %.6f%n", taskNumber, left, right, step, result);
            } catch (IllegalArgumentException e) {
                System.out.printf("Task %d - Ошибка интегрирования: %s%n", taskNumber, e.getMessage());
            }
        }
    }

    // Метод потокового выполнения (Задание 3)
    public static void simpleThreads() {
        Task task = new Task();
        task.setTaskCount(100);

        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));

        generatorThread.setPriority(Thread.MAX_PRIORITY);
        integratorThread.setPriority(Thread.MIN_PRIORITY);

        generatorThread.start();
        integratorThread.start();

        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все задачи сгенерированы и интегрированы потоками.");
    }

    // Метод для Задания 4
    public static void complicatedThreads() {
        Task task = new Task();
        task.setTaskCount(100);

        Semaphore semaphore = new Semaphore();

        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        generator.start();
        integrator.start();

        try {
            // Ждём завершения потоков (без прерывания)
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Все задачи успешно обработаны с использованием семафора.");
    }

}
