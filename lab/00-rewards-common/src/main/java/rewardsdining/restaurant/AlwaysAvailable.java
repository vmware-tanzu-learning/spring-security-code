package rewardsdining.restaurant;

import com.fasterxml.jackson.annotation.JsonValue;

import rewardsdining.account.Account;

/**
 * A benefit availabilty policy that returns true at all times.
 */
public class AlwaysAvailable implements BenefitAvailabilityPolicy {
	static final BenefitAvailabilityPolicy INSTANCE = new AlwaysAvailable();

	public boolean isBenefitAvailableFor(Account account, Dining dining) {
		return true;
	}

	@JsonValue
	public String toString() {
		return "alwaysAvailable";
	}
}
