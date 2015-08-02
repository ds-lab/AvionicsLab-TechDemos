package mutex.monitor;

public interface ThreadSafeBlockingQueue<T> {

	/**
	 * Adds a new element to this queue.
	 * 
	 * @param element
	 *            the element to add, must not be null
	 */
	void put(T element);

	/**
	 * Removes the first element from this queue.
	 * 
	 * If the queue is currently empty, the method will block until an element
	 * becomes available.
	 * 
	 * @return the first element from the queue
	 * @throws InterruptedException
	 *             if the operation was interrupted while waiting for an element
	 *             to become available for removal
	 */
	T take() throws InterruptedException;

	/**
	 * Returns the number of elements in this queue.
	 * 
	 * @return
	 */
	int size();
}