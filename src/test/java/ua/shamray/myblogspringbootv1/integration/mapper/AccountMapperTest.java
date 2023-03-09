package ua.shamray.myblogspringbootv1.integration.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.mapper.AccountMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {
    @Autowired
    private AccountMapper accountMapper;
    private Account account;
    private AccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        accountDTO = AccountDTO.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
    }

    @Test
    void entityToDTO() {
        //given
        AccountDTO expected = accountDTO;
        //when
        AccountDTO resultAccountDTO = accountMapper.entityToDTO(account);
        //then
        assertEquals(expected.getId(), resultAccountDTO.getId());
        assertEquals(expected.getFirstName(), resultAccountDTO.getFirstName());
        assertEquals(expected.getLastName(), resultAccountDTO.getLastName());
        assertEquals(expected.getEmail(), resultAccountDTO.getEmail());
        assertNull(resultAccountDTO.getPassword());
    }

    @Test
    void dtoToEntity() {
        //given
        Account expected = account;

        //when
        Account resultAccount = accountMapper.dtoToEntity(accountDTO);

        //then
        assertNotNull(resultAccount.getRoles());
        assertNotNull(resultAccount.getPostList());
        assertNull(resultAccount.getId());
        assertEquals(resultAccount.getEmail(), expected.getEmail());
        assertEquals(resultAccount.getFirstName(), expected.getFirstName());
        assertEquals(resultAccount.getLastName(), expected.getLastName());
        assertEquals(resultAccount.getPassword(), expected.getPassword());
    }

    @Test
    void entityToViewer() {
        //given
        AccountViewer expectedAccountViewer = AccountViewer.builder()
                .email(account.getEmail())
                .build();

        //when
        AccountViewer resultAccountViewer = accountMapper.entityToViewer(account);

        //then
        assertEquals(expectedAccountViewer.getEmail(), resultAccountViewer.getEmail());
    }

}
