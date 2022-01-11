package rewardsdining.reward.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
public class RewardsControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testRewardsWithAdmin() throws Exception {
		mockMvc
		.perform(get("/rewards"))
		.andExpect(status().isOk());
	}
}
