package mutex.monitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue<T> implements ThreadSafeBlockingQueue<T> {
	final Lock putLock = new ReentrantLock();
	final Lock takeLock = new ReentrantLock();
	final Condition notEmpty = takeLock.newCondition();
	final Queue<T> queue = new LinkedList<T>();

	@Override
	public void put(T element) {
		putLock.lock();
		try {
			queue.add(element);

			takeLock.lock();
			try {
				// Allows blocked readers to continue
				notEmpty.signal();
			} finally {
				takeLock.unlock();
			}

		} finally {
			putLock.unlock();
		}
	}

	@Override
	public T take() throws InterruptedException {
		putLock.lockInterruptibly();
		takeLock.lockInterruptibly();

		try {
			// Wait until an element becomes available
			while (size() == 0) {
				System.out.println("Waiting until an element becomes available.");
				notEmpty.await();
			}

			return queue.remove();
		} finally {
			takeLock.unlock();
			putLock.unlock();
		}
	}

	@Override
	public int size() {
		putLock.lock();
		try {
			return queue.size();
		} finally {
			putLock.unlock();
		}
	}
}
