package rewardsdining.restaurant;

import com.fasterxml.jackson.annotation.JsonValue;

import rewardsdining.account.Account;

/**
 * A benefit availabilty policy that returns false at all times.
 */
public class NeverAvailable implements BenefitAvailabilityPolicy {
	static final BenefitAvailabilityPolicy INSTANCE = new NeverAvailable();

	public boolean isBenefitAvailableFor(Account account, Dining dining) {
		return false;
	}

	@JsonValue
	public String toString() {
		return "neverAvailable";
	}
}
