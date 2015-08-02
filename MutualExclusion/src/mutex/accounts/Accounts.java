package mutex.accounts;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Accounts {
	private static final Random random = new Random();

	public static void main(String[] args) throws InterruptedException {
		int expectedBalance = 1000;
		final Account account = new NaiveAccount(expectedBalance);

		final ExecutorService exec = Executors.newFixedThreadPool(4);

		for (int i = 0; i < 10000; ++i) {
			final int amountToDeposit = random.nextInt(50);
			final int amountToWithdraw = random.nextInt(20) + 1;
			expectedBalance += amountToDeposit - amountToWithdraw;

			exec.execute(() -> account.deposit(amountToDeposit));
			exec.execute(() -> account.withdraw(amountToWithdraw));
		}

		exec.shutdown();
		exec.awaitTermination(100, TimeUnit.MILLISECONDS);

		if (account.getBalance() != expectedBalance) {
			System.out.printf("Race occurred, actual balance $%d did not match expected balance $%d.\n",
					account.getBalance(), expectedBalance);
		} else {
			System.out.println("No race occurred.");
		}
	}
}
