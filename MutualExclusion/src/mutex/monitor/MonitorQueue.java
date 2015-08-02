package mutex.monitor;

import java.util.LinkedList;
import java.util.Queue;

public class MonitorQueue<T> implements ThreadSafeBlockingQueue<T> {
	private final Queue<T> queue = new LinkedList<>();

	@Override
	public void put(T element) {
		synchronized (this) {
			System.out.println("Adding element to queue: " + element);
			queue.add(element);
			this.notifyAll();
		}
	}

	@Override
	public T take() throws InterruptedException {
		synchronized (this) {
			while (queue.isEmpty()) {
				System.out.println("Waiting until an element becomes available.");
				this.wait();
			}
			return queue.remove();
		}
	}

	@Override
	public int size() {
		synchronized (this) {
			return queue.size();
		}
	}
}
