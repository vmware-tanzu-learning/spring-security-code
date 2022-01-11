package rewardsdining.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.PersonContextMapper;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import rewardsdining.security.CustomAuthenticationSuccessHandler;


@Configuration
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(requests -> requests
				.mvcMatchers("/login", "/logout").permitAll()
				.anyRequest().authenticated())
			.httpBasic(withDefaults())
			.formLogin(login -> login.loginPage("/login")
						.usernameParameter("user")
						.passwordParameter("pass")
						.successHandler(new CustomAuthenticationSuccessHandler()))
			.logout(withDefaults());
		return http.build();
	}
	
	@Bean
	public UserDetailsService inMemoryAuthentication() {
		UserDetails user = User.builder()
			.username("keith")
			.password("{bcrypt}$2a$10$/ebEcjNSEgX89NM1fTI.H.B2px3rb14n4shYCTwUFGHWF83UHBPbm")
			.roles("USER")
			.build();
		UserDetails admin = User.builder()
			.username("chad")
			.password("{bcrypt}$2a$10$QCpjm5rVx9OW7oXka/tI0eayc0ckDHAMiZde0fjaIRLMWsNS0K4TS")
			.roles("USER", "ADMIN")
			.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	
	//@Bean
	public UserDetailsManager jdbcAuthentication(DataSource dataSource) {
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, 'true' as enabled from T_ACCOUNT where username = ?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role_name as authority from T_ACCOUNT a inner join T_ACCOUNT_ROLE ar on (a.id = ar.account_id) where a.username = ?");
		return jdbcUserDetailsManager;
	}
	
	//@Bean
	public BindAuthenticator bindAuthenticator(BaseLdapPathContextSource contextSource) {
		BindAuthenticator authenticator = new BindAuthenticator(contextSource);
		authenticator.setUserDnPatterns(new String[] { "uid={0},ou=people,dc=rewardsdining,dc=org" });
		return authenticator;
	}
	
	//@Bean
	public LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator authenticator, ContextSource contextSource) {
		var authoritiesPopulator =  new DefaultLdapAuthoritiesPopulator(contextSource, "ou=groups,dc=rewardsdining,dc=org");
		authoritiesPopulator.setGroupRoleAttribute("ou");
		
		LdapAuthenticationProvider provider = new LdapAuthenticationProvider(authenticator, authoritiesPopulator);
		provider.setUserDetailsContextMapper(new PersonContextMapper());
		return provider;
	}
	
	@Bean
	public InMemoryAuditEventRepository repository(){
		return new InMemoryAuditEventRepository();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
}

