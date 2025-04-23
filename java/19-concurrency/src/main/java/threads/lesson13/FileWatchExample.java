package threads.lesson13;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileWatchExample {

    public static void main(String[] args) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directory = Paths.get(".");
        WatchKey watchKey = directory.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY,
            StandardWatchEventKinds.ENTRY_DELETE
        );

        boolean keepGoing = true;
        while (keepGoing) {
            try {
                watchKey = watchService.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent<?> event : events) {
                Path context = (Path) event.context();
                System.out.printf("Event type: %s - Context: %s%n", event.kind(), context);
                if (context.getFileName().toString().equals("Testing.txt")
                    && event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                    System.out.println("Shuttting down watch service");
                    watchService.close();
                    keepGoing = false;
                    break;
                }
            }
            watchKey.reset();
        }

    }
}
