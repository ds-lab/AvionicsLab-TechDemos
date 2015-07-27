import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class WatchServiceDemo {
    public static void main(String[] args) throws IOException,
            InterruptedException {
        try (final WatchService watcher = FileSystems
                .getDefault()
                .newWatchService()) {
            final Path resourceDir = Paths.get("resources");
            final Map<WatchKey, Path> rootDirs = new HashMap<WatchKey, Path>();

            WatchKey registeredKey = resourceDir.register(watcher,
                    ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            rootDirs.put(registeredKey, resourceDir);

            boolean abort = false;
            while (!abort) {
                final WatchKey key = watcher.take();
                for (WatchEvent<?> ev : key.pollEvents()) {
                    final Kind<?> kind = ev.kind();

                    // We can't do anything about the OVERFLOW event
                    if (kind.equals(OVERFLOW)) {
                        continue;
                    }

                    /*
                     * All other event types come with the path of the changed
                     * entry stored in the context field. However, these are
                     * relative to the watched directory, so we have to resolve
                     * them relative to the watched base directory using
                     * Path.resolve().
                     */
                    @SuppressWarnings("unchecked") final WatchEvent<Path> event = (WatchEvent<Path>) ev;
                    final Path rootDir = rootDirs.get(key);
                    final Path path = rootDir.resolve(event.context());

                    if (kind.equals(ENTRY_CREATE)) {
                        System.out.println("Created entry: " + path);

                        /*
                         * If a new directory was created inside the watched
                         * directory, also watch out for its events.
                         */
                        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                            registeredKey = path.register(watcher,
                                    ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                            rootDirs.put(registeredKey, path);
                        }
                    } else if (kind.equals(ENTRY_DELETE)) {
                        System.out.println("Deleted path: " + path);
                    } else if (kind.equals(ENTRY_MODIFY)) {
                        System.out.println("Modified entry: " + path);
                    }
                }

                abort = !key.reset();
            }
        }
    }
}
