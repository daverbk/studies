package threads.task1.training;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        var oddNumbersThread = new Thread(() -> {
            Stream.iterate(1, num -> num + 1)
                    .filter(num -> num % 2 != 0)
                    .limit(5)
                    .forEach(num -> {
                        var threadName = Thread.currentThread().getName();
                        System.out.printf("Odd numbers thread -- %s -- %d\n", threadName, num);
                    });
        });
        var evenNumbersThread = new TrainingEvenNumbersThread();

        oddNumbersThread.start();
        evenNumbersThread.start();
    }
}