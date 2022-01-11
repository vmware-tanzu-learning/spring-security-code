package rewardsdining.account.web;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import common.money.Percentage;
import rewardsdining.account.Account;
import rewardsdining.account.AccountManager;
import rewardsdining.account.Beneficiary;

@RestController
public class AccountRestController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final AccountManager accountManager;

	/**
	 * Creates a new AccountController with a given account manager.
	 */
	public AccountRestController(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Provide a list of all accounts.
	 */
	@GetMapping(value = "/accounts")
	public List<Account> accountSummary() {
		return accountManager.getAllAccounts();
	}

	/**
	 * Provide the details of an account with the given id.
	 */
	@GetMapping(value = "/accounts/{id}")
	public Account accountDetails(@PathVariable int id) {
		return retrieveAccount(id);
	}

	/**
	 * Creates a new Account, setting its URL as the Location header on the
	 * response.
	 */
	@PostMapping(value = "/accounts")
	public ResponseEntity<Void> createAccount(@RequestBody Account newAccount) {
		Account account = accountManager.save(newAccount);
		return entityWithLocation(account.getEntityId());
	}

	/**
	 * Returns the Beneficiary with the given name for the Account with the
	 * given id.
	 */
	@GetMapping(value = "/accounts/{accountId}/beneficiaries/{beneficiaryName}")
	public Beneficiary getBeneficiary(@PathVariable("accountId") int accountId,
			@PathVariable("beneficiaryName") String beneficiaryName) {
		return retrieveAccount(accountId).getBeneficiary(beneficiaryName);
	}

	/**
	 * Adds a Beneficiary with the given name to the Account with the given id,
	 * setting its URL as the Location header on the response.
	 */
	@PostMapping(value = "/accounts/{accountId}/beneficiaries")
	public ResponseEntity<Void> addBeneficiary(@PathVariable long accountId, @RequestBody String beneficiaryName) {
		accountManager.addBeneficiary(accountId, beneficiaryName);
		return entityWithLocation(beneficiaryName);
	}

	/**
	 * Removes the Beneficiary with the given name from the Account with the
	 * given id.
	 */
	@DeleteMapping(value = "/accounts/{accountId}/beneficiaries/{beneficiaryName}")
	@ResponseStatus(HttpStatus.NO_CONTENT) // 204
	public void removeBeneficiary(@PathVariable long accountId, @PathVariable String beneficiaryName) {
		Account account = this.retrieveAccount(accountId);
		
		Beneficiary deletedBeneficiary = account.getBeneficiary(beneficiaryName);

		var allocationPercentages = new HashMap<String, Percentage>();

		// If we are removing the only beneficiary or the beneficiary has an
		// allocation of zero we don't need to worry. Otherwise, need to share
		// out the benefit of the deleted beneficiary amongst all the others
		if (account.getBeneficiaries().size() != 1
				&& (!deletedBeneficiary.getAllocationPercentage().equals(Percentage.zero()))) {
			// This logic is very simplistic, doesn't account for rounding errors
			Percentage p = deletedBeneficiary.getAllocationPercentage();
			int remaining = account.getBeneficiaries().size() - 1;
			double extra = p.asDouble() / remaining;

			for (Beneficiary beneficiary : account.getBeneficiaries()) {
				if (beneficiary != deletedBeneficiary) {
					double newValue = beneficiary.getAllocationPercentage().asDouble() + extra;
					allocationPercentages.put(beneficiary.getName(), new Percentage(newValue));
				}
			}
		}

		accountManager.removeBeneficiary(accountId, beneficiaryName, allocationPercentages);
	}

	/**
	 * Maps UnsupportedOperationException to a 501 Not Implemented HTTP status
	 * code.
	 */
	@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler({ UnsupportedOperationException.class })
	public void handleUnabletoReallocate(Exception ex) {
		logger.error("Exception is: ", ex);
		// just return empty 501
	}

	/**
	 * Maps IllegalArgumentExceptions to a 404 Not Found HTTP status code.
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(IllegalArgumentException.class)
	public void handleNotFound(Exception ex) {
		logger.error("Exception is: ", ex);
		// return empty 404
	}

	/**
	 * Maps DataIntegrityViolationException to a 409 Conflict HTTP status code.
	 */
	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public void handleAlreadyExists(Exception ex) {
		logger.error("Exception is: ", ex);
		// return empty 409
	}

	/**
	 * Finds the Account with the given id, throwing an IllegalArgumentException
	 * if there is no such Account.
	 */
	private Account retrieveAccount(long accountId) throws IllegalArgumentException {
		return accountManager.getAccount(accountId)
				.orElseThrow(() -> new IllegalArgumentException("No such account with id " + accountId));
	}

	/**
	 * Return a response with the location of the new resource. It's URL is
	 * assumed to be a child of the URL just received.
	 *
	 * Suppose we have just received an incoming URL of, say,
	 * http://localhost:8080/accounts and resourceId
	 * is "12345". Then the URL of the new resource will be
	 * http://localhost:8080/accounts/12345.
	 * 
	 * @param resourceId
	 *            Is of the new resource.
	 * @return
	 */
	private ResponseEntity<Void> entityWithLocation(Object resourceId) {

		// Determines URL of child resource based on the full URL of the given
		// request, appending the path info with the given resource Identifier
		URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{childId}").buildAndExpand(resourceId)
				.toUri();

		// Return an HttpEntity object - it will be used to build the
		// HttpServletResponse
		return ResponseEntity.created(location).build();
	}

}
