package rewardsdining.restaurant.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class RestaurantDto {

	private String name;
	
	private String location;
	
	@Min(0) @Max(1)
	private double benefitPercentage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getBenefitPercentage() {
		return benefitPercentage;
	}

	public void setBenefitPercentage(double benefitPercentage) {
		this.benefitPercentage = benefitPercentage;
	}
}
