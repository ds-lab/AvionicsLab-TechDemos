package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
	public static void main(String[] args) throws IOException, InterruptedException {
		final Server server = new Server();
		System.out.println("Waiting for connections...");

		// Initialize the selector
		final Selector selector = Selector.open();

		// Create a regular server socket...
		final ServerSocketChannel socketChannel = ServerSocketChannel.open();
		final InetSocketAddress addr = new InetSocketAddress("localhost", 9876);
		socketChannel.bind(addr);
		socketChannel.configureBlocking(false);

		// ... and register it with the selector
		final int ops = socketChannel.validOps();
		socketChannel.register(selector, ops);

		while (true) {
			// Block, until at least one channel is ready
			final int numKeys = selector.select();

			for (SelectionKey key : selector.selectedKeys()) {
				if (key.isAcceptable()) {
					// Accept incoming connections from clients
					final SocketChannel client = socketChannel.accept();
					if (client == null) {
						continue;
					}

					System.out.println("Got connection from: " + client);

					// Add the client socket to the selector for reading
					client.configureBlocking(false);
					client.register(selector, SelectionKey.OP_READ);
				} else if (key.isReadable()) {
					// Data available on one client socket
					final SocketChannel client = (SocketChannel) key.channel();

					// Read data from the socket and convert to UTF-8 string
					final ByteBuffer buffer = ByteBuffer.allocate(1024);
					client.read(buffer);
					buffer.rewind();

					final String data = StandardCharsets.UTF_8.decode(buffer).toString().trim();

					if ("END".equals(data)) {
						System.out.println("Closing connection to " + client);
						client.close();
					}

					System.out.println(client + " says: " + data);
				}
			}
		}
	}
}
