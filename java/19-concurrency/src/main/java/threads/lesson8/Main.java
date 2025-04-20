package threads.lesson8;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        var dtf = DateTimeFormatter.ofLocalizedDateTime(
            FormatStyle.MEDIUM, FormatStyle.LONG
        );

        Callable<ZonedDateTime> waitThenDoIt = () -> {
            ZonedDateTime zdt = null;
            try {
                TimeUnit.SECONDS.sleep(2);
                zdt = ZonedDateTime.now();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return zdt;
        };

        var threadPool = Executors.newFixedThreadPool(2);
        List<Callable<ZonedDateTime>> tasks = Collections.nCopies(4, waitThenDoIt);

        try {
            System.out.println("---> " + ZonedDateTime.now().format(dtf));
            List<Future<ZonedDateTime>> futureList = threadPool.invokeAll(tasks);
            for (Future<ZonedDateTime> future : futureList) {
                try {
                    System.out.println(future.get().format(dtf));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }

        Runnable dateTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println("a " +
                    ZonedDateTime.now().format(dtf)
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        System.out.println("---> " + ZonedDateTime.now().format(dtf));
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

        var scheduledTask = executor.scheduleAtFixedRate(
            dateTask,
            2,
            2,
            TimeUnit.SECONDS
        );

        var scheduledTask2 = executor.scheduleAtFixedRate(
            () -> System.out.println("b " + ZonedDateTime.now().format(dtf)),
            2,
            2,
            TimeUnit.SECONDS
        );

        long time = System.currentTimeMillis();
        while (!scheduledTask.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(2);
                if ((System.currentTimeMillis() - time) / 1000 > 10) {
                    scheduledTask.cancel(true);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown();
    }
}
