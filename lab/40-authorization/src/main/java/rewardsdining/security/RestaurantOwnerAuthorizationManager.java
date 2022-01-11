package rewardsdining.security;

import java.util.Set;
import java.util.function.Supplier;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import rewardsdining.restaurant.RestaurantManager;

@Component
public class RestaurantOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

	private final RestaurantManager restaurantManager;
	
	
	public RestaurantOwnerAuthorizationManager(RestaurantManager restaurantManager) {
		this.restaurantManager = restaurantManager;
	}
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> auth, RequestAuthorizationContext authContext) {
		long restaurantId = Long.parseLong(authContext.getVariables().get("id"));
		Authentication authentication = auth.get();
		
		Set<String> userAuthorities = SecurityUtils.getAuthorities(authentication);

			
		return null;
	}
}