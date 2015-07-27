package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A simple consumer that reads a UTF8-encoded message from a channel and prints
 * it to standard output until interrupted.
 *
 * @author Adrian Rumpold (a.rumpold@gmail.com)
 */
public class Consumer implements Runnable {
    private final ReadableByteChannel channel;

    public Consumer(ReadableByteChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        // All incoming messages are assumed to be UTF8-encoded
        final Charset utf8 = StandardCharsets.UTF_8;

        try {
            while (!Thread.interrupted()) {
                final ByteBuffer bytes = ByteBuffer.allocate(4096);
                channel.read(bytes);
                bytes.rewind();

                final String message = utf8.decode(bytes).toString();
                System.out.println(message);
            }
        } catch (ClosedByInterruptException ignore) {
            // Thread interrupt is expected to signal shutdown
            System.out.println("Consumer shutting down.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
