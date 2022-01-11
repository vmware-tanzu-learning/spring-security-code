package rewardsdining.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@SpringBootTest
public class RestaurantOwnerAuthorizationManagerTests {

	@Autowired 
	RestaurantOwnerAuthorizationManager authorizationManager;
	
	@Test
	public void testAdminShouldBeGranted() {
		var decision = checkWithUser("admin",  "ROLE_ADMIN");
		
		assertThat(decision.isGranted()).isTrue();
	}
	
	@Test
	public void testOwnerShouldNotBeGranted() {
		var decision = checkWithUser("dollie",  "ROLE_MANAGER");
		
		assertThat(decision.isGranted()).isTrue();
	}
	
	@Test
	public void testUserShouldNotBeGranted() {
		var decision = checkWithUser("user",  "ROLE_USER");
		
		assertThat(decision.isGranted()).isFalse();
	}
	
	@Test
	public void testManagerShouldNotBeGranted() {
		var decision = checkWithUser("manager",  "ROLE_MANAGER");
		
		assertThat(decision.isGranted()).isFalse();
	}
	
	private AuthorizationDecision checkWithUser(String principal, String... roles) {
		Supplier<Authentication> auth = () -> new TestingAuthenticationToken(principal, null, roles);
		var context = new RequestAuthorizationContext(new MockHttpServletRequest(), Map.of("id", "1"));
		
		return authorizationManager.check(auth, context);
	}
}
