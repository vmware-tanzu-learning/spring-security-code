package rewardsdining.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)
			throws Exception {
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers(EndpointRequest.toAnyEndpoint().excluding("health")).hasRole("ADMIN")
				.mvcMatchers(HttpMethod.PUT, "/restaurants/{id}").hasAnyRole("MANAGER", "ADMIN")
				.mvcMatchers("/restaurants/**").hasRole("USER")
				.mvcMatchers("/rewards").hasRole("ADMIN")
				.anyRequest().authenticated())
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
}
