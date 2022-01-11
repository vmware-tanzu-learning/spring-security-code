package rewardsdining.restaurant;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import common.money.MonetaryAmount;
import common.money.Percentage;
import rewardsdining.account.Account;

/**
 * A restaurant establishment in the network. Like AppleBee's.
 * 
 * Restaurants calculate how much benefit may be awarded to an account for
 * dining based on an availability policy and a benefit percentage.
 */
@Entity
@Table(name = "T_RESTAURANT")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"owner", "new", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"})
public class Restaurant extends AbstractAuditable<Account, Long> {


	@Column(name = "MERCHANT_NUMBER")
	private String number;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LOCATION")
	private String location;
	
	@Embedded
	@AttributeOverride(name = "value", column = @Column(name="BENEFIT_PERCENTAGE"))
	private Percentage benefitPercentage;

	/**
	 * This class needs special mapping via its own accessor methods - see:
	 * <ul>
	 * <li> {@link #getDbBenefitAvailabilityPolicy()}</li>
	 * <li> {@link #setDbBenefitAvailabilityPolicy(String)}</li>
	 * </ul>
	 * It is marked transient to hide it from the default field mapping
	 * mechanism.
	 */
	@Transient
	//@Column(name = "BENEFIT_AVAILABILITY_POLICY")
	private BenefitAvailabilityPolicy benefitAvailabilityPolicy;
	
	@ManyToOne
	private Account owner;

	protected Restaurant() {
	}

	/**
	 * Creates a new restaurant.
	 * 
	 * @param number
	 *            the restaurant's merchant number
	 * @param name
	 *            the name of the restaurant
	 */
	public Restaurant(String number, String name) {
		this.number = number;
		this.name = name;
	}

	/**
	 * Sets the percentage benefit to be awarded for eligible dining
	 * transactions.
	 * 
	 * @param benefitPercentage
	 *            the benefit percentage
	 */
	public void setBenefitPercentage(Percentage benefitPercentage) {
		this.benefitPercentage = benefitPercentage;
	}

	/**
	 * Sets the policy that determines if a dining by an account at this
	 * restaurant is eligible for benefit.
	 * 
	 * @param benefitAvailabilityPolicy
	 *            the benefit availability policy
	 */
	public void setBenefitAvailabilityPolicy(
			BenefitAvailabilityPolicy benefitAvailabilityPolicy) {
		this.benefitAvailabilityPolicy = benefitAvailabilityPolicy;
	}

	/**
	 * Returns the merchant number of this restaurant.
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Returns the name of this restaurant.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this restaurant.
	 * 
	 *  @param name 
	 *  			the name of the restaurant
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the location of this restaurant.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location of this restaurant.
	 * 
	 *  @param location 
	 *  			the location of the restaurant
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	/**
	 * Returns the owner of this restaurant.
	 */
	public Account getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of this restaurant.
	 * 
	 *  @param owner 
	 *  			the owner of the restaurant
	 */
	public void setOwner(Account owner) {
		this.owner = owner;
	}

	/**
	 * Returns this restaurant's benefit percentage.
	 */
	public Percentage getBenefitPercentage() {
		return benefitPercentage;
	}

	/**
	 * Returns this restaurant's benefit availability policy.
	 */
	public BenefitAvailabilityPolicy getBenefitAvailabilityPolicy() {
		return benefitAvailabilityPolicy;
	}

	/**
	 * Calculate the benefit eligible to this account for dining at this
	 * restaurant.
	 * 
	 * @param account
	 *            the account that dined at this restaurant
	 * @param dining
	 *            a dining event that occurred
	 * @return the benefit amount eligible for reward
	 */
	public MonetaryAmount calculateBenefitFor(Account account, Dining dining) {
		if (benefitAvailabilityPolicy.isBenefitAvailableFor(account, dining)) {
			return dining.getAmount().multiplyBy(benefitPercentage);
		} else {
			return MonetaryAmount.zero();
		}
	}

	// Internal methods for JPA only - hence they are protected.
	/**
	 * Sets this restaurant's benefit availability policy from the code stored
	 * in the underlying column. This method is a database specific accessor
	 * using the JPA 2 @Access annotation.
	 */
	@Access(AccessType.PROPERTY)
	@Column(name = "BENEFIT_AVAILABILITY_POLICY")
	protected void setDbBenefitAvailabilityPolicy(String policyCode) {
		if ("A".equals(policyCode)) {
			benefitAvailabilityPolicy = AlwaysAvailable.INSTANCE;
		} else if ("N".equals(policyCode)) {
			benefitAvailabilityPolicy = NeverAvailable.INSTANCE;
		} else {
			throw new IllegalArgumentException("Not a supported policy code "
					+ policyCode);
		}
	}

	/**
	 * Returns this restaurant's benefit availability policy code for storage in
	 * the underlying column. This method is a database specific accessor using
	 * the JPA 2 @Access annotation.
	 */
	@Access(AccessType.PROPERTY)
	@Column(name = "BENEFIT_AVAILABILITY_POLICY")
	protected String getDbBenefitAvailabilityPolicy() {
		if (benefitAvailabilityPolicy == AlwaysAvailable.INSTANCE) {
			return "A";
		} else if (benefitAvailabilityPolicy == NeverAvailable.INSTANCE) {
			return "N";
		} else {
			throw new IllegalArgumentException("No policy code for "
					+ benefitAvailabilityPolicy.getClass());
		}
	}

	public String toString() {
		return "Number = '" + number + "', name = '" + name
				+ "', benefitPercentage = " + benefitPercentage
				+ ", benefitAvailabilityPolicy = " + benefitAvailabilityPolicy;
	}
}