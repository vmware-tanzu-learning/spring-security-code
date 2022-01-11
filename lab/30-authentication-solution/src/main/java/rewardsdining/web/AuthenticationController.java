package rewardsdining.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

	@GetMapping("/login")
	public String login() {
		return "custom-login";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "custom-logout";
	}
}