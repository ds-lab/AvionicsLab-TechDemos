package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public class NioDemo {
	private static Producer producer;
	private static Consumer consumer;

	/** Connect producer and consumer using a Pipe */
	private static void doPipe() throws IOException {
		final Pipe pipe = Pipe.open();

		producer = new Producer("Hello world", pipe.sink());
		consumer = new Consumer(pipe.source());
	}

	/** Connect producer and consumer via a TCP network channel */
	private static void doTcp() throws IOException {
		final InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 9999);

		final ServerSocketChannel consumerChannel = ServerSocketChannel.open();
		consumerChannel.socket().bind(socketAddress);

		producer = new Producer("Hello world", SocketChannel.open(socketAddress));
		consumer = new Consumer(consumerChannel.accept());
	}

	public static void main(String[] args) {
		try {
			doPipe();
			// doTcp();

			final ExecutorService executor = Executors.newFixedThreadPool(2);

			executor.submit(producer);
			executor.submit(consumer);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executor.shutdownNow();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
