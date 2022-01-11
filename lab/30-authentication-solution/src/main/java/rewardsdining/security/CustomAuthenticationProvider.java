package rewardsdining.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import rewardsdining.account.data.AccountRepository;

//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final CustomUserDetailsService userDetailsService;
	
	private final PasswordEncoder passwordEncoder;
	
	
	public CustomAuthenticationProvider(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.userDetailsService = new CustomUserDetailsService(accountRepository);
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String username = authentication.getName();
		
		String password = authentication.getCredentials().toString();
		
		try {
			UserDetails user = userDetailsService.loadUserByUsername(username);
			
			if(passwordEncoder.matches(password, user.getPassword())) {
				return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
			} else {
				throw new BadCredentialsException("Bad Credentials");
			}
			
		} catch (UsernameNotFoundException unfe){
			throw new BadCredentialsException(unfe.getMessage());
		}
	}
	
    @Override
    public boolean supports(Class<?> authentication) {
    	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
