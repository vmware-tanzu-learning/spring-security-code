package rewardsdining.reward;

import java.time.LocalDate;

import common.money.MonetaryAmount;
import rewardsdining.restaurant.Restaurant;

public class Reward {

	private long id;
	
	private String confirmationNumber;
	
	private MonetaryAmount amount;
	
	private LocalDate rewardDate;
	
	private String accountNumber;
	
	private Restaurant restaurant;
	
	private LocalDate diningDate;
	
	private MonetaryAmount diningAmount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getConfirmationNumber() {
		return confirmationNumber;
	}

	public void setConfirmationNumber(String confirmationNumber) {
		this.confirmationNumber = confirmationNumber;
	}

	public MonetaryAmount getAmount() {
		return amount;
	}

	public void setAmount(MonetaryAmount amount) {
		this.amount = amount;
	}

	public LocalDate getRewardDate() {
		return rewardDate;
	}

	public void setRewardDate(LocalDate rewardDate) {
		this.rewardDate = rewardDate;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public LocalDate getDiningDate() {
		return diningDate;
	}

	public void setDiningDate(LocalDate diningDate) {
		this.diningDate = diningDate;
	}

	public MonetaryAmount getDiningAmount() {
		return diningAmount;
	}

	public void setDiningAmount(MonetaryAmount diningAmount) {
		this.diningAmount = diningAmount;
	}
}
