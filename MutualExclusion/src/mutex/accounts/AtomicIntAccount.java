package mutex.accounts;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntAccount implements Account {
	private AtomicInteger balance;

	public AtomicIntAccount(int initialBalance) {
		balance = new AtomicInteger(initialBalance);
	}

	@Override
	public int deposit(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount to deposit must be positive");
		}

		return balance.addAndGet(amount);
	}

	@Override
	public int withdraw(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount to withdraw must be positive");
		}

		if (amount > balance.get()) {
			throw new IllegalArgumentException("Amount to withdraw must be less than the current account balance");
		}

		return balance.addAndGet(-amount);
	}

	@Override
	public int getBalance() {
		return balance.get();
	}
}
