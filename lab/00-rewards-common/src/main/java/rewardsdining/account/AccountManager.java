package rewardsdining.account;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.money.Percentage;
import rewardsdining.account.data.AccountRepository;

/**
 * Manages access to account information. Used as the service layer component in
 * the <tt>mvc</tt> and <tt>security</tt> projects.
 */
@Service
@Transactional
public class AccountManager {

	private final AccountRepository accountRepository;
	
	public AccountManager(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}


	@Transactional(readOnly = true)
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	
	@Transactional(readOnly = true)
	public Optional<Account> getAccount(Long id) {
		return accountRepository.findById(id);
	}

	
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	
	public void update(Account account) {
		accountRepository.save(account);
	}

	 
	public void updateBeneficiaryAllocationPercentages(Long accountId, Map<String, Percentage> allocationPercentages) {
		this.getAccount(accountId).ifPresent(account -> {
			for (Entry<String, Percentage> entry : allocationPercentages.entrySet()) {
				account.getBeneficiary(entry.getKey()).setAllocationPercentage(entry.getValue());
			}
		});
	}

	
	public void addBeneficiary(Long accountId, String beneficiaryName) {
		this.getAccount(accountId)
			.ifPresent(account -> account.addBeneficiary(beneficiaryName, Percentage.zero()));
	}

	public void removeBeneficiary(Long accountId, String beneficiaryName,
			Map<String, Percentage> allocationPercentages) {
		this.getAccount(accountId).ifPresent(account -> {
			
			account.removeBeneficiary(beneficiaryName);
			
			if (allocationPercentages != null)
				updateBeneficiaryAllocationPercentages(accountId, allocationPercentages);
			}
		);
	}
		
}
