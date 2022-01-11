package rewardsdining.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;
import java.util.TreeMap;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import rewardsdining.security.RestaurantOwnerAuthorizationManager;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, RestaurantOwnerAuthorizationManager authorizationManager)
			throws Exception {
		http
			.authorizeRequests(requests -> requests
				.mvcMatchers("/").permitAll()
				.requestMatchers(EndpointRequest.toAnyEndpoint().excluding("health")).hasRole("ADMIN")
				//.mvcMatchers(HttpMethod.PUT, "/restaurants/{id}").hasAnyRole("MANAGER", "ADMIN")
				.mvcMatchers(HttpMethod.PUT, "/restaurants/{id}").access("hasRole('ADMIN') || (hasRole('MANAGER') && @restaurantAuthorizer.isOwner(authentication, #id))")
				.mvcMatchers("/restaurants/**").hasAnyRole("USER", "MANAGER", "ADMIN")
				.mvcMatchers("/accounts/**").hasRole("ADMIN")
				.anyRequest().authenticated())
//			.authorizeHttpRequests(requests -> requests
//					.mvcMatchers("/").permitAll()
//					.requestMatchers(EndpointRequest.toAnyEndpoint().excluding("health")).hasRole("ADMIN")
//					.mvcMatchers(HttpMethod.PUT, "/restaurants/{id}").access(authorizationManager)
//					.mvcMatchers("/restaurants/**").hasAnyRole("USER", "MANAGER", "ADMIN")
//					.mvcMatchers("/accounts/**").hasRole("ADMIN")
//					.anyRequest().authenticated())
			.csrf().disable()
			.httpBasic(withDefaults())
			.formLogin(withDefaults())
			.logout(withDefaults());
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.antMatchers("/h2-console/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		var roleHierarchyMap = new TreeMap<String, List<String>>();
		roleHierarchyMap.put("ROLE_ADMIN", List.of("ROLE_MANAGER", "ROLE_USER"));

		roleHierarchyMap.put("ROLE_MANAGER", List.of("ROLE_USER"));

		String stringHierarychy = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);

		var roleHierarchy = new RoleHierarchyImpl();

		roleHierarchy.setHierarchy(stringHierarychy);

		return roleHierarchy;

	}
}
