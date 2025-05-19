package threads.locks.practice;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.HashMap;
import java.util.Map;

public class ThreadSafeCache {

    private final Map<String, String> cache = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public String get(String key) {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading: " + key);
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public void put(String key, String value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " writing: " + key);
            cache.put(key, value);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ThreadSafeCache cache = new ThreadSafeCache();

        Runnable writer = () -> {
            for (int i = 0; i < 5; i++) {
                cache.put("key" + i, "value" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        };

        Runnable reader = () -> {
            for (int i = 0; i < 5; i++) {
                cache.get("key" + i);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
        };

        Thread t1 = new Thread(writer, "Writer-Thread");
        Thread t2 = new Thread(writer, "Writer-Thread-2");
        Thread t3 = new Thread(reader, "Reader-1");
        Thread t4 = new Thread(reader, "Reader-2");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
