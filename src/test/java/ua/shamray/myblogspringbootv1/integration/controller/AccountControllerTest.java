package ua.shamray.myblogspringbootv1.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.shamray.myblogspringbootv1.controller.AccountController;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.service.AccountService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountService accountService;
    @Test
    public void givenValidAccountDTO_whenRegisterNewAccount_thenReturnsStatusOk() throws Exception {
        // given
        AccountDTO accountDTO = AccountDTO.builder()
                .firstName("first")
                .lastName("last")
                .email("email@mail.com")
                .password("pass")
                .build();
        AccountDTO newAccountDTO = AccountDTO.builder()
                .id(1L)
                .firstName("first")
                .lastName("last")
                .email("email@mail.com")
                .password("pass")
                .build();

        when(accountService.saveNewUser(any(AccountDTO.class))).thenReturn(newAccountDTO);
        // when
        MvcResult mvcResult = mockMvc.perform(post("/blog/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andReturn();
        // then
        String responseJson = mvcResult.getResponse().getContentAsString();
        AccountDTO newUser = objectMapper.readValue(responseJson, AccountDTO.class);
        assertNotNull(newUser.getId());
        assertEquals(accountDTO.getFirstName(), newUser.getFirstName());
        assertEquals(accountDTO.getLastName(), newUser.getLastName());
        assertEquals(accountDTO.getEmail(), newUser.getEmail());
    }

    @Test
    public void givenInvalidAccountDTO_whenRegisterNewAccount_thenReturns400BadRequest() throws Exception {
        // given
        AccountDTO accountDTO = AccountDTO.builder()
                .firstName(" ")
                .lastName(" ")
                .email(" ")
                .password(" ")
                .build();

        // when
        mockMvc.perform(post("/blog/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isNotAcceptable());
    }

}