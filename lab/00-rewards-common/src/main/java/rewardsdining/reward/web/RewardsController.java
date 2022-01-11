package rewardsdining.reward.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import rewardsdining.reward.RewardNetwork;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class RewardsController {

	private final RewardNetwork rewardNetwork;
	
	
	public RewardsController(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}

	@GetMapping("/my-rewards")
	public String showMyRewards(Model model) {
		model.addAttribute("rewards", rewardNetwork.findCurrentUserRewards());
		return "rewards/my";
	}
	
	@GetMapping("/rewards") 
	public String showRewards(Model model) {
		model.addAttribute("rewards", rewardNetwork.findRewards());
		return "rewards/list";
	}
}
