package rewardsdining.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import rewardsdining.account.data.AccountRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;
	
	
	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return accountRepository.findByUsername(username)
				.map(AccountUserDetails::from)
				.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
	}
}