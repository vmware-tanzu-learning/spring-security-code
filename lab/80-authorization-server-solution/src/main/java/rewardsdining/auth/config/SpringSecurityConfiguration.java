package rewardsdining.auth.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@EnableWebSecurity
public class SpringSecurityConfiguration {
 
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorizeRequests -> authorizeRequests
					.anyRequest().permitAll())
			.formLogin(Customizer.withDefaults());

		return http.build();
	}
 
  @Bean
  public UserDetailsService users() {
    UserDetails keith = User.withDefaultPasswordEncoder()
        .username("keith")
        .password("spring")
        .roles("USER").build();
    
    UserDetails chad = User.withDefaultPasswordEncoder()
            .username("chad")
            .password("spring")
            .roles("USER", "ADMIN").build();
    
    return new InMemoryUserDetailsManager(keith, chad);
  }
  
	@Bean
	public WebSecurityCustomizer ignoringCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.antMatchers("/h2-console/**");
	}
	
	@Bean
	public WebMvcConfigurer configurer(){
	  return new WebMvcConfigurer(){
	    @Override
	    public void addCorsMappings(CorsRegistry registry) {
	      registry.addMapping("/*")
	          .allowedOrigins("*");
	    }
	  };
	}
 
}