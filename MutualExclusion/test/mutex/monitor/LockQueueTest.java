package mutex.monitor;

import org.junit.Before;

public class LockQueueTest extends ThreadSafeBlockingQueueTestBase {
	@Before
	public void setUp() {
		queue = new LockQueue<>();
	}
}
