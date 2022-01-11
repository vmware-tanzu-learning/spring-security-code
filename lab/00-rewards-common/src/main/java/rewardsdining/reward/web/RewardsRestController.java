package rewardsdining.reward.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rewardsdining.reward.Reward;
import rewardsdining.reward.RewardNetwork;

@RestController
public class RewardsRestController {

	private final RewardNetwork rewardNetwork;
	
	
	public RewardsRestController(RewardNetwork rewardNetwork) {
		this.rewardNetwork = rewardNetwork;
	}

	@GetMapping("/my-rewards")
	public List<Reward> showMyRewards() {
		return rewardNetwork.findCurrentUserRewards();
	}
	
	@GetMapping("/rewards") 
	public List<Reward> showRewards() {
		return rewardNetwork.findRewards();
	}
}
