package rewardsdining.reward;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import common.money.MonetaryAmount;
import rewardsdining.account.Account;
import rewardsdining.account.AccountContribution;
import rewardsdining.account.data.AccountRepository;
import rewardsdining.restaurant.Dining;
import rewardsdining.restaurant.Restaurant;
import rewardsdining.restaurant.RestaurantRepository;
import rewardsdining.security.SecurityUtils;

/**
 * Rewards a member account for dining at a restaurant.
 * 
 * A reward takes the form of a monetary contribution made to an account that is distributed among the account's
 * beneficiaries. The contribution amount is typically a function of several factors such as the dining amount and
 * restaurant where the dining occurred.
 * 
 * Example: Papa Keith spends $100.00 at Apple Bee's resulting in a $8.00 contribution to his account that is
 * distributed evenly among his beneficiaries Annabelle and Corgan.
 * 
 * This is the central application-boundary for the "rewards" application. This is the class users call to
 * invoke the application. This is the entry-point into the Application Layer.
 */
@Service
public class RewardNetwork {

	private final AccountRepository accountRepository;

	private final RestaurantRepository restaurantRepository;

	private final RewardRepository rewardRepository;

	/**
	 * Creates a new reward network.
	 * @param accountRepository the repository for loading accounts to reward
	 * @param restaurantRepository the repository for loading restaurants that determine how much to reward
	 * @param rewardRepository the repository for recording a record of successful reward transactions
	 */
	public RewardNetwork(AccountRepository accountRepository, RestaurantRepository restaurantRepository,
			RewardRepository rewardRepository) {
		this.accountRepository = accountRepository;
		this.restaurantRepository = restaurantRepository;
		this.rewardRepository = rewardRepository;
	}

	/**
	 * Reward an account for dining.
	 * 
	 * For a dining to be eligible for reward: - It must have been paid for by a registered credit card of a valid
	 * member account in the network. - It must have taken place at a restaurant participating in the network.
	 * 
	 * @param dining a charge made to a credit card for dining at a restaurant
	 * @return confirmation of the reward
	 */
	@Transactional
	public RewardConfirmation rewardAccountFor(Dining dining) {
		Account account = accountRepository.findByCreditCardNumber(dining.getCreditCardNumber());
		Restaurant restaurant = restaurantRepository.findByNumber(dining.getMerchantNumber());
		MonetaryAmount amount = restaurant.calculateBenefitFor(account, dining);
		AccountContribution contribution = account.makeContribution(amount);
		return rewardRepository.confirmReward(contribution, dining);
	}
	
	public List<Reward> findRewards() {
		return rewardRepository.findAll();
	}
	
	public List<Reward> findCurrentUserRewards() {
		return SecurityUtils.getCurrentAuthentication()
				.flatMap(auth -> accountRepository.findByUsername(auth.getName()))
				.map(Account::getNumber)
				.map(rewardRepository::findByAccountNumber)
				.orElseGet(() -> Collections.emptyList());
	}
}