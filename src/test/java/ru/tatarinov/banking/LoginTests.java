package ru.tatarinov.banking;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user-create-before.sql", "/card-create-before.sql", "/transaction-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/transaction-create-after.sql", "/card-create-after.sql", "/user-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LoginTests {
	@Autowired
	private MockMvc mockMvc;


	@Test
	public void loginRedirect() throws Exception{
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/auth/login"));
	}

	@Test
	public void correctLoginTest() throws Exception{
		this.mockMvc.perform(formLogin("/process_login").user("user").password("password"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clients/default"));
	}

	@Test
	public void badCredentials() throws Exception{
		this.mockMvc.perform(post("/process_login").param("username", "user").param("password", "qqq"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/auth/login?error"));
	}


}
