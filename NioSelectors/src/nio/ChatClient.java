package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {
	public static void main(String[] args) throws IOException {
		// Open a socket connection to the server
		final InetSocketAddress addr = new InetSocketAddress("localhost", 9876);
		final SocketChannel client = SocketChannel.open(addr);
		client.configureBlocking(false);

		// Scanner is be used to read from the console
		// Note: try-with-resources block avoids resource leak
		try (final Scanner scanner = new Scanner(System.in)) {
			System.out.println("Enter messages to send to the server (disconnect with END).");
			while (client.isConnected()) {
				System.out.print(">> ");

				// Read a line of input from the user and encode it as UTF-8
				final String message = scanner.nextLine();
				final ByteBuffer buffer = StandardCharsets.UTF_8.encode(message);

				// Send the encoded message to the server
				if (client.isConnected()) {
					client.write(buffer);
				}
			}
		}
	}
}
