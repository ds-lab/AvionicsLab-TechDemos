package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A simple producer that sends a UTF8-encoded message over a channel until
 * interrupted.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class Producer implements Runnable {
    private final String message;
    private final WritableByteChannel channel;

    public Producer(String message, WritableByteChannel channel) {
        if (!channel.isOpen()) {
            throw new IllegalStateException(
                    "Communication channel must be open");
        }

        this.message = message;
        this.channel = channel;
    }

    @Override
    public void run() {
        // All outgoing data will be encoded as UTF8
        final Charset cs = StandardCharsets.UTF_8;
        int counter = 0;

        try {
            while (!Thread.interrupted()) {
                Thread.sleep(1000);

                final ByteBuffer bytes = cs.encode(message + " " + counter++);
                channel.write(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException ignore) {
            // Thread interrupt is expected to signal shutdown
            System.out.println("Producer shutting down.");
        }
    }
}
