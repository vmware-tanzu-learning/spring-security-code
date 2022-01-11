package rewardsdining.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import rewardsdining.account.data.AccountRepository;

@Component
public class UpgradePasswordService implements UserDetailsPasswordService {

	private final AccountRepository accountRepository;
	

	public UpgradePasswordService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		return accountRepository.findByUsername(user.getUsername())
				.map(account -> {
					account.setPassword(newPassword);
					accountRepository.save(account);
					return AccountUserDetails.from(account);
				})
				.map(UserDetails.class::cast)
				.orElse(user);
	}

}
