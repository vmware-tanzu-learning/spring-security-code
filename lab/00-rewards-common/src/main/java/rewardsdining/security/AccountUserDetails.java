package rewardsdining.security;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import rewardsdining.account.Account;
import rewardsdining.account.Role;

public class AccountUserDetails extends Account implements UserDetails, OidcUser, CredentialsContainer {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> attributes;
	
    private Map<String, Object> claims;
    
    private OidcUserInfo userInfo;
    
    private OidcIdToken idToken;
	
	public AccountUserDetails(Account account) {
		setEntityId(account.getEntityId());
		setNumber(account.getNumber());
		setName(account.getName());
		setUsername(account.getUsername());
		setPassword(account.getPassword());
		setRoles(account.getRoles());
		getRoles().size(); // Initialize roles in case they haven't been loaded
		super.beneficiaries = account.getBeneficiaries();
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream()
				.map(Role::getName)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void eraseCredentials() {
		this.setPassword(null);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getClaims() {
		return claims;
	}

	public void setClaims(Map<String, Object> claims) {
		this.claims = claims;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(OidcUserInfo userInfo) {
		this.userInfo = userInfo;
	}


	@Override
	public OidcIdToken getIdToken() {
		return idToken;
	}
	
	public void setIdToken(OidcIdToken idToken) {
		this.idToken = idToken;
	}
	
	
	public static AccountUserDetails from(Account account) {
		return  new AccountUserDetails(account);
	
	}
	
	public static AccountUserDetails from(Account account, OAuth2User oauth2User) {
		var accountUserDetails =  from(account);
		
		accountUserDetails.setAttributes(oauth2User.getAttributes());
		
		return accountUserDetails;
	}
	
	public static AccountUserDetails fromOidc(Account account, OidcUser oidcUser) {
		var accountUserDetails =  from(account, oidcUser);
		
		accountUserDetails.setAttributes(oidcUser.getAttributes());
		accountUserDetails.setClaims(oidcUser.getClaims());
		accountUserDetails.setIdToken(oidcUser.getIdToken());
		accountUserDetails.setUserInfo(oidcUser.getUserInfo());
		
		return accountUserDetails;
	}


	@Override
	public String toString() {
		return "AccountUserDetails [entityId=" + entityId + ", number=" + number + ", name=" + name
				+ ", username=" + username + ", password=" + password + ", roles="
				+ roles + ", attributes=" + attributes + ", claims=" + claims + ", userInfo=" + userInfo
						+ ", idToken=" + idToken + ", ]";
	}
	
	
}
