package rewardsdining.restaurant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import common.money.Percentage;

@SpringBootTest
public class RestaurantManagerTests {

	@Autowired
	private RestaurantManager restaurantManager;
	
	@AfterEach
	public void cleanup() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	public void testUserShouldNotBeAllowToUpdate() {
		setupSecurityContext("user", "ROLE_USER");
		
		var restaurant = restaurantManager.findById(1L).get();
		restaurant.setBenefitPercentage(new Percentage(0.2));
		assertThrows(AccessDeniedException.class, () -> restaurantManager.save(restaurant));
	}
	
	@Test
	public void testManagerShouldNotBeAllowToUpdate() {
		setupSecurityContext("manager", "ROLE_MANAGER");
		
		var restaurant = restaurantManager.findById(1L).get();
		restaurant.setBenefitPercentage(new Percentage(0.2));
		assertThrows(AccessDeniedException.class, () -> restaurantManager.save(restaurant));
	}
	
	@Test
	public void testAdminShouldBeAllowedToUpdate() {
		setupSecurityContext("admin", "ROLE_ADMIN");
		
		var restaurant = restaurantManager.findById(1L).get();
		restaurant.setBenefitPercentage(new Percentage(0.2));
		assertDoesNotThrow(() -> restaurantManager.save(restaurant));
	}
	
	@Test
	public void testOwnerShouldBeAllowToUpdate() {
		setupSecurityContext("dollie", "ROLE_MANAGER");
		
		var restaurant = restaurantManager.findById(1L).get();
		restaurant.setBenefitPercentage(new Percentage(0.2));
		assertDoesNotThrow(() -> restaurantManager.save(restaurant));
	}
	
	private void setupSecurityContext(String principal, String... roles) {
		var ctx = SecurityContextHolder.createEmptyContext();
		var auth = new TestingAuthenticationToken(principal, null, roles);
		ctx.setAuthentication(auth);
		SecurityContextHolder.setContext(ctx);
	}
}
