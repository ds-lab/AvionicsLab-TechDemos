package mutex.monitor;

import org.junit.Before;

public class MonitorQueueTest extends ThreadSafeBlockingQueueTestBase {
	@Before
	public void setUp() {
		queue = new MonitorQueue<>();
	}
}
