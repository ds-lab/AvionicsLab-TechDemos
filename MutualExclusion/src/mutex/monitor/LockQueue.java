package mutex.monitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue<T> implements ThreadSafeBlockingQueue<T> {
	final Semaphore readSemphore = new Semaphore(0);
	final Lock lock = new ReentrantLock();
	final Queue<T> queue = new LinkedList<T>();

	@Override
	public void put(T element) {
		lock.lock();
		try {
			queue.add(element);
			readSemphore.release();
		} finally {
			lock.unlock();
		}

		// Allows blocked readers to continue
	}

	@Override
	public T take() throws InterruptedException {
		readSemphore.acquire();
		lock.lockInterruptibly();
		try {
			return queue.remove();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int size() {
		lock.lock();
		final int size = queue.size();
		lock.unlock();
		return size;
	}
}
