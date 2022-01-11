package rewardsdining.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PasswordEncoderTests {
	
	private static final String TEST_USERNAME = "sergi";
	
	private static final String PASSWORD = "springsecurity";
	
	private static final String ENCODED_PASSWORD = "{bcrypt}$2a$10$BOmUkBbtePvoJ0RY9DsrdefcKkFdrkDD35tIiM4skcFk8Vf8umnuu";
	

	@MockBean
	private UserDetailsService userDetailsService;
	
	@SpyBean
	private UserDetailsPasswordService passwordService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Test
	public void testUpgradeFromDefaultStrength(@Autowired PasswordEncoder strongerPasswordEncoder) {
		PasswordEncoder defaultPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		String defaultEncoderPassword = defaultPasswordEncoder.encode("password");

		assertThat(strongerPasswordEncoder.upgradeEncoding(defaultEncoderPassword)).isTrue();
	}
	
	@Test
	@Transactional
	public void testUpgradePassword() {
		var auth = new TestingAuthenticationToken(TEST_USERNAME, PASSWORD);
		var user = User.withUsername(TEST_USERNAME).password(ENCODED_PASSWORD).build();
		
		when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(user);

		authenticationManager.authenticate(auth);
		
		verify(passwordService).updatePassword(user, any());
	}
}
