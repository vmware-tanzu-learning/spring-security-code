package rewardsdining.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import rewardsdining.account.Account;
import rewardsdining.account.data.AccountRepository;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
	
	private final AccountRepository accountRepository;
	
	
	public CustomOAuth2UserService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}


    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        return null;
    }
  
    private AccountUserDetails createAccount(OAuth2User oauth2User) {
    	Account account = new Account(oauth2User.getName());
    	account.setUsername(oauth2User.getName());
    	accountRepository.save(account);
    	return AccountUserDetails.from(account, oauth2User);
    }
}
