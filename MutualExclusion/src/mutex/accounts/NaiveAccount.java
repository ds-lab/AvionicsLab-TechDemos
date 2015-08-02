package mutex.accounts;

public class NaiveAccount implements Account {
	private int balance;

	public NaiveAccount(int initialBalance) {
		balance = initialBalance;
	}

	@Override
	public int deposit(int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount to deposit must be positive");
		}

		return balance += amount;
	}

	@Override
	public int withdraw(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount to withdraw must be positive");
		}

		if (amount > balance) {
			throw new IllegalArgumentException("Amount to withdraw must be less than the current account balance");
		}

		return balance -= amount;
	}

	@Override
	public int getBalance() {
		return balance;
	}
}
