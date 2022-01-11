package rewardsdining.account.data;

import rewardsdining.account.Account;

public interface CustomAccountRepository {
	/**
	 * Load an account by its credit card.
	 * @param creditCardNumber the credit card number
	 * @return the account object
	 */
	public Account findByCreditCardNumber(String creditCardNumber);
}