package rewardsdining.security;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("@restaurantAuthorizer")
public @interface IsOwner {

}
