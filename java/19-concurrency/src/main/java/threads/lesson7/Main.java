package threads.lesson7;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ColorThreadFactory implements ThreadFactory {



    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}

public class Main {

    public static void main(String[] args) {

        var blueExecutor = Executors.newSingleThreadExecutor();
        blueExecutor.execute(Main::countDown);
        blueExecutor.shutdown();
    }

    public static void notmain(String[] args) {

        Thread blue = new Thread(
            Main::countDown, ThreadColor.ANSI_BLUE.name()
        );

        Thread yellow = new Thread(
            Main::countDown, ThreadColor.ANSI_YELLOW.name()
        );

        Thread red = new Thread(
            Main::countDown, ThreadColor.ANSI_RED.name()
        );

        blue.start();

        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        yellow.start();

        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        red.start();

        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void countDown() {

        String threadName = Thread.currentThread().getName();
        var threadColor = ThreadColor.ANSI_RESET;
        try {
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch (IllegalArgumentException ignore) {
        }
        String color = threadColor.getColor();
        for (int i = 20; i >= 0; i--) {
            System.out.println(
                color + " " + threadName.replace("ANSI_", "") + "  " + i
            );
        }
    }

}
