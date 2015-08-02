package mutex.monitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue<T> implements ThreadSafeBlockingQueue<T> {
	final Lock lock = new ReentrantLock();
	final Queue<T> queue = new LinkedList<T>();

	@Override
	public void put(T element) {
		lock.lock();
		queue.add(element);
		lock.unlock();
	}

	@Override
	public T take() throws InterruptedException {
		lock.lockInterruptibly();
		final T result = queue.remove();
		lock.unlock();
		return result;
	}

	@Override
	public int size() {
		lock.lock();
		final int size = queue.size();
		lock.unlock();
		return size;
	}
}
