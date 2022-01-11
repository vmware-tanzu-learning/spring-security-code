package rewardsdining.security;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import rewardsdining.account.Account;

public class SecurityUtils {

	public static Optional<Account> getCurrentUser() {
		return getCurrentAuthentication()
			      .filter(Authentication::isAuthenticated)
			      .map(Authentication::getPrincipal)
			      .filter(Account.class::isInstance)
			      .map(Account.class::cast);
	}
	
	public static Set<String> getAuthorities(Authentication authentication) {
		return Optional.ofNullable(authentication)
			      .map(Authentication::getAuthorities)
			      .map(AuthorityUtils::authorityListToSet)
			      .orElse(Collections.<String>emptySet());
	}
	
	public static Set<String> getCurrentAuthorities() {
		return getCurrentAuthentication()
			      .map(Authentication::getAuthorities)
			      .map(AuthorityUtils::authorityListToSet)
			      .orElse(Collections.<String>emptySet());
	}
	
	public static Optional<Authentication> getCurrentAuthentication() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
			      .map(SecurityContext::getAuthentication);
	}
}
