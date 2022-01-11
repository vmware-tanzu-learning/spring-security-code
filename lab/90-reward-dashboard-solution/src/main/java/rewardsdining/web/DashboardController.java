package rewardsdining.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Controller
public class DashboardController {
	
	private final WebClient webClient;
	
	private final String rewardsDiningBaseUrl;
	
	public DashboardController(WebClient webClient, @Value("${rewards-dining.url}") String rewardsDiningBaseUrl) {
		this.webClient = webClient;
		this.rewardsDiningBaseUrl = rewardsDiningBaseUrl;
	}
	
	@GetMapping("/")
	public String dashboard() {
		return "dashboard";
	}
	
	@GetMapping("/rewards")
	public @ResponseBody Mono<String> rewards() {
		return webClient
		        .get()
		        .uri(rewardsDiningBaseUrl + "/rewards")
		        .retrieve()
		        .onStatus(
		                s -> s.equals(HttpStatus.UNAUTHORIZED),
		                cr -> Mono.just(new BadCredentialsException("Token not valid")))
		        .onStatus(
		                s -> s.equals(HttpStatus.FORBIDDEN),
		                cr -> Mono.just(new AccessDeniedException("Not authorized")))
		        .bodyToMono(String.class);
	}
	
	@GetMapping("/user-info")  
    public String userInfo(Model model,  
                              @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,  
                              @AuthenticationPrincipal OidcUser oauth2User) {
        model.addAttribute("username", oauth2User.getName());
        model.addAttribute("idToken", oauth2User.getIdToken().getTokenValue());
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        
        model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());  
        model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());
        
        return "user-info";  
    }	
}