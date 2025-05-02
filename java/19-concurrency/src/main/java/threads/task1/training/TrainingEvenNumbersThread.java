package threads.task1.training;

import java.util.stream.Stream;

public class TrainingEvenNumbersThread extends Thread {

    @Override
    public void run() {
        Stream.iterate(1, num -> num + 1)
            .filter(num -> num % 2 == 0)
            .limit(5)
                .forEach(num -> {
                    var threadName = Thread.currentThread().getName();
                    System.out.printf("Even numbers thread -- %s -- %d\n", threadName, num);
                });
    }
}