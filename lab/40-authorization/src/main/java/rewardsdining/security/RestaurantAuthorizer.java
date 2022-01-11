package rewardsdining.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import rewardsdining.restaurant.RestaurantManager;

@Component
public class RestaurantAuthorizer {

	private final RestaurantManager restaurantManager;
	
	
	public RestaurantAuthorizer(RestaurantManager restaurantManager) {
		this.restaurantManager = restaurantManager;
	}

	
}
