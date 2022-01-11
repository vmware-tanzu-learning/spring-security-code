package rewardsdining.account.data;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import rewardsdining.account.Account;

/**
 * An account repository that uses JPA to find accounts.
 */
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

	public static final String ACCOUNT_BY_CC_QUERY = "select ACCOUNT_ID from T_ACCOUNT_CREDIT_CARD where NUMBER = :ccn";

	private EntityManager entityManager;

	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Account findByCreditCardNumber(String creditCardNumber) {
		// Find id account of account with this credit-card using a direct
		// SQL query on the unmapped T_ACCOUNT_CREDIT_CARD table.
		Integer accountId = (Integer) entityManager
				.createNativeQuery(ACCOUNT_BY_CC_QUERY)
				.setParameter("ccn", creditCardNumber).getSingleResult();

		Account account = (Account) entityManager.find(Account.class, accountId.longValue());

		// Force beneficiaries to load too - avoid Hibernate lazy loading error
		account.getBeneficiaries().size();

		return account;
	}

}