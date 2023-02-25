package ru.tatarinov.banking;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("user")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user-create-before.sql", "/card-create-before.sql", "/transaction-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/transaction-create-after.sql", "/card-create-after.sql", "/user-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CardControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showTest() throws Exception{
        mockMvc.perform(get("/cards/1"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='card-id']").exists())
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='card-id']").string("Счет: 1"));
    }

    @Test
    public void addCartTest() throws Exception{
        mockMvc.perform(post("/cards/new/1"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated());

        mockMvc.perform(get("/clients/1"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='cardList']/div").nodeCount(3))
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='cardList']/div[@id=10]").exists());
    }

}
