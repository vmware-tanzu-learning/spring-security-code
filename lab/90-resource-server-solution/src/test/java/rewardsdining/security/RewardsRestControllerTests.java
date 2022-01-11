package rewardsdining.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.web.servlet.MockMvc;

import rewardsdining.config.SecurityConfig;
import rewardsdining.reward.RewardNetwork;
import rewardsdining.reward.web.RewardsRestController;

@WebMvcTest(RewardsRestController.class)
@Import(SecurityConfig.class)
public class RewardsRestControllerTests {
	
	@MockBean
	private RewardNetwork rewardNetwork;
	
	@Autowired
	private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
	
	@Autowired
	private MockMvc mockMvc;

	
	@Test
	public void tetsRewardsEnpointValidJwtAuthorities() throws Exception {
		mockMvc
			.perform(get("/rewards")
					 .with(jwt()
							 .jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN"))
							 .authorities(jwtGrantedAuthoritiesConverter)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void tetsRewardsEnpointInvalidJwtAuthorities() throws Exception {
		mockMvc
		.perform(get("/rewards")
				 .with(jwt()
						 .jwt(jwt -> jwt.claim("authorities", "ROLE_USER"))
						 .authorities(jwtGrantedAuthoritiesConverter)))
		.andExpect(status().isForbidden());
	}
	
}