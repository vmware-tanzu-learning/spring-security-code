package rewardsdining.restaurant;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import common.money.Percentage;
import rewardsdining.account.data.AccountRepository;

@SpringBootTest
public class RestaurantAuditorTests {

	private static final long TEST_RESTAURANT_ID = 1L;
	
	private static final String TEST_ACCOUNT_USERNAME = "sergi";
	
	@Autowired
	private AccountRepository accountRepository;
	
	@BeforeEach
	public void setupSecurityContext() {
		accountRepository.findByUsername(TEST_ACCOUNT_USERNAME).ifPresent(account -> {
			var ctx = SecurityContextHolder.createEmptyContext();
			var auth = new TestingAuthenticationToken(account, "", "ROLE_ADMIN");
			ctx.setAuthentication(auth);
			SecurityContextHolder.setContext(ctx);
		});
	}
	
	@AfterEach
	public void cleanup() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@Transactional
	public void testRestaurantLastModifiedAuditor(@Autowired RestaurantRepository restaurantRepository) {
		var restaurant = restaurantRepository.getById(TEST_RESTAURANT_ID);
		restaurant.setBenefitPercentage(new Percentage(0.2));
		
		var savedRestaurant = restaurantRepository.saveAndFlush(restaurant);
		
		assertThat(savedRestaurant.getLastModifiedBy().isPresent()).isTrue();
		assertThat(savedRestaurant.getLastModifiedBy().get().getUsername()).isEqualTo(TEST_ACCOUNT_USERNAME);
	}
}
