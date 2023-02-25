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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.tatarinov.banking.controllers.ClientsController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("user")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/user-create-before.sql", "/card-create-before.sql", "/transaction-create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/transaction-create-after.sql", "/card-create-after.sql", "/user-create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientsController controller;

    @Test
    public void clientMainPageTest() throws Exception{
        mockMvc.perform(get("/clients/1"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.xpath("//p[@id='username']").string("Клиент: user"));
    }

    @Test
    public void clientCardListTest() throws Exception{
        mockMvc.perform(get("/clients/1"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.xpath("//div[@id='cardList']/div").nodeCount(2));
    }

    @Test
    public void transferTest() throws Exception{
        mockMvc.perform(get("/clients/1/transaction"))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.xpath("//select[@id='cardFrom']/option").nodeCount(2));
    }

//    @Test
//    public void transferConfirmationTest() throws Exception{
//        MockHttpServletRequestBuilder patch = patch("/clients/1/transaction")
//                .param("source", "1")
//                .param("destination", "3")
//                .param("amount", "50");
//        mockMvc.perform(patch)
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.xpath("//input[@id='source.id'][@value='1']").exists());
//    }

}
