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


	public boolean isOwner(Authentication authentication, long restaurantId) {
		return restaurantManager.findById(restaurantId)
				.map(restaurant -> authentication.getName().equals(restaurant.getOwner().getUsername()))
				.orElse(false);
	}	
}
