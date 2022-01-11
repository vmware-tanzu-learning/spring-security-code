package rewardsdining.security;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import rewardsdining.account.data.AccountRepository;

@Component
public class AuthenticationEventListener {

	private final AccountRepository accountRepository;
	
	public AuthenticationEventListener(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@EventListener
	public void onSuccessfulAuthentication(InteractiveAuthenticationSuccessEvent event) {
		accountRepository.findByUsername(event.getAuthentication().getName())
			.ifPresent(account -> {
				account.setLastLogin(LocalDateTime.now());
				accountRepository.save(account);
			});
	}
}
