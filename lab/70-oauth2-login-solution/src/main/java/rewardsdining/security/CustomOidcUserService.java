package rewardsdining.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import rewardsdining.account.data.AccountRepository;

public class CustomOidcUserService extends OidcUserService {

	private final AccountRepository accountRepository;
	
	
	public CustomOidcUserService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}



	@Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		
		//oidcUser.get
		//return accountRepository.findByUsername(userRequest.get)
		return null;
	}
	
	public String getUsername(OidcUser oidcUser) {
		return oidcUser.getAttribute(StandardClaimNames.EMAIL);
	}
	
}
