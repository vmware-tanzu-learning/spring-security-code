package rewardsdining.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import rewardsdining.account.Account;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Account> {

	@Override
	public Optional<Account> getCurrentAuditor() {
		return SecurityUtils.getCurrentUser();
	}
}