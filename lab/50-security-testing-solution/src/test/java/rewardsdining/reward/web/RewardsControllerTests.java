package rewardsdining.reward.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RewardsControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testRewardsWithAdmin() throws Exception {
		mockMvc
		.perform(get("/rewards").with(user("admin").roles("ADMIN")))
		.andExpect(status().isOk());
	}
}
