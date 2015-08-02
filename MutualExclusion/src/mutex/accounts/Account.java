package mutex.accounts;

public interface Account {
	/**
	 * Make a deposit into this account.
	 * 
	 * @param amount
	 *            amount to deposit, must be positive
	 * @return the new account balance after the deposit
	 */
	int deposit(int amount);

	/**
	 * Make a withdrawal from this account.
	 * 
	 * @param amount
	 *            amount to withdraw, must be positive and less than the current
	 *            account balance
	 * @return the new account balance after the deposit
	 */
	int withdraw(int amount);

	/**
	 * @return the current account balance
	 */
	int getBalance();
}