package rewardsdining.auth.config;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class AuthoritiesTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
	
	private static final String AUTHORITIES_CLAIM = "authorities";
	
	private static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	private static final int ADMIN_TOKEN_EXPIRATION = 15 * 60; // 900 seconds
	
	private static final int USER_TOKEN_EXPIRATION = 60 * 60; // 3600 seconds
	
	@Override
	public void customize(JwtEncodingContext context) {
		Set<String> authorities = getAuthoritySet(context);

		context.getClaims().claim(AUTHORITIES_CLAIM, authorities);
		
		int tokenExpiration = authorities.contains(ROLE_ADMIN) ? ADMIN_TOKEN_EXPIRATION : USER_TOKEN_EXPIRATION;
		
		context.getClaims().expiresAt(calculateTokenExpiration(tokenExpiration));
	}
	
	private Set<String> getAuthoritySet(JwtEncodingContext context) {
		return context.getPrincipal().getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}
	
	private Instant calculateTokenExpiration(int seconds) {
		return Instant.now().plusSeconds(seconds);
	}
}
