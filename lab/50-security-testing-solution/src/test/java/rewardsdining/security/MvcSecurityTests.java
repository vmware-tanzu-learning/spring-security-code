package rewardsdining.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class MvcSecurityTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testSuccessfulFormLogin () throws Exception {
		mockMvc.perform(formLogin().user("keith").password("spring"))
		.andExpect(authenticated().withUsername("keith").withRoles("USER"));
	}
	
	@Test
	public void testSuccessfulBasicAuthentication () throws Exception {
		mockMvc.perform(get("/").with(httpBasic("keith", "spring")))
			.andExpect(authenticated().withUsername("keith").withRoles("USER"));

	}
	
}
		