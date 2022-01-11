package rewardsdining.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import rewardsdining.restaurant.Restaurant;
import rewardsdining.restaurant.RestaurantManager;

@Component
public class RestaurantAuthorizer {

	private final RestaurantManager restaurantManager;
	
	
	public RestaurantAuthorizer(RestaurantManager restaurantManager) {
		this.restaurantManager = restaurantManager;
	}


	public boolean isOwner(Authentication authentication, long restaurantId) {
		return restaurantManager.findById(restaurantId)
				.map(restaurant -> isOwner(authentication, restaurant))
				.orElse(false);
	}	
	
	public boolean isOwner(Authentication authentication, Restaurant restaurant) {
		return  authentication.getName().equals(restaurant.getOwner().getUsername());
	}
}
