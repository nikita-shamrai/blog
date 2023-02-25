package ua.shamray.myblogspringbootv1.service.impl.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.mapper.AccountMapper;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.RoleService;
import ua.shamray.myblogspringbootv1.service.impl.AccountServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "command.line.runner.enabled=false")
class AccountServiceImplUnitTest {
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private RoleService roleService;
    @SpyBean
    private PasswordEncoder passwordEncoder;
    @SpyBean
    private AccountMapper accountMapper;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl( accountRepository,
                                            roleService,
                                            passwordEncoder,
                                            accountMapper);
    }

    @Test
    void canConvertAccountDTOToEntity() {
        //given
        AccountDTO accountDTO = AccountDTO.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        Account account = accountMapper.dtoToEntity(accountDTO);
        //then
        assertThat(account.getId()).isNull();
        assertThat(account.getFirstName()).isEqualTo("firstName");
        assertThat(account.getLastName()).isEqualTo("lastName");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getEmail()).isEqualTo("findMe@mail.com");
    }
    @Test
    void canConvertAccountEntityToDTO() {
        //given
        Account account = Account.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        AccountDTO accountDTO = accountMapper.entityToDTO(account);
        //then
        assertThat(accountDTO.getId()).isEqualTo(1L);
        assertThat(accountDTO.getFirstName()).isEqualTo("firstName");
        assertThat(accountDTO.getLastName()).isEqualTo("lastName");
        assertThat(accountDTO.getPassword()).isNull();
        assertThat(accountDTO.getEmail()).isEqualTo("findMe@mail.com");
    }
    @Test
    void canGetAllPosts() {
        //when
        accountService.getAll();
        //then
        verify(accountRepository).findAll();
    }

//is it Ok?
    @Test
    void canSaveNewUserIfAccountDTOProvided() {
        //given
        AccountDTO accountDTO = AccountDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        when(accountRepository
                .save(any(Account.class)))
                .thenReturn(
                        Account.builder()
                                .id(1L)
                                .build()
                );
        AccountDTO newUserDTO = accountService.saveNewUser(accountDTO);
        //then
        assertThat(newUserDTO.getId()).isEqualTo(1L);
    }


    @Test
    void checkFindByEmailRepoCalledInFindByEmailService() {
        //given
        String email = "findMe@mail.com";
        //when
        accountService.findByEmail(email);
        //then
        verify(accountRepository).findByEmail(email);    }


}