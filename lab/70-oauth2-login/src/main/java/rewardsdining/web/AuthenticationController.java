package rewardsdining.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticationController {

	@GetMapping("/login")
	public String login() {
		return "custom-login";
	}
	
	@RequestMapping("/secured")  
    public String securedPage(Model model,  
                              @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,  
                              @AuthenticationPrincipal OidcUser oauth2User) {  
        model.addAttribute("userName", oauth2User.getName());  
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());  
        model.addAttribute("token", authorizedClient.getAccessToken().getTokenValue());  
        model.addAttribute("idtoken", oauth2User.getIdToken().getTokenValue());
        
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        return "secure";  
    }  
}