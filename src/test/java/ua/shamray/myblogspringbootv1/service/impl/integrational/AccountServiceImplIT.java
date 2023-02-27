package ua.shamray.myblogspringbootv1.service.impl.integrational;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.repository.RoleRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "command.line.runner.enabled=true")
class AccountServiceImplIT {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountService accountService;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void throwsExceptionIfUserWithSameEmailAlreadyExists(){
        //given
        AccountDTO accountDTO = AccountDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        AccountDTO accountDTOWithSameEmail = AccountDTO.builder()
                .firstName("anotherFirstName")
                .lastName("anotherLastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        accountService.saveNewUser(accountDTO);
        //then
        assertThrows(IllegalArgumentException.class, () -> accountService.saveNewUser(accountDTOWithSameEmail));
    }

    @Test
    void settingAdminRoleToExistingUser() {
        //given
        Role userRole = roleRepository.findById("ROLE_USER").orElseThrow();

        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(new HashSet<>(List.of(userRole)))
                .build();

        accountRepository.save(account);
        //when
        accountService.setUserAsAdmin(account);
        //then
        AtomicBoolean contains = new AtomicBoolean(false);
        account.getRoles().forEach(role -> {
                if(role.getRoleType().equals(RoleType.ROLE_ADMIN)){
                    contains.set(true);
        }});
        assertThat(contains).isTrue();
    }

    @Test
    @WithMockUser(username="findMe@mail.com", roles={"USER"})
    void getCurrentAuthenticatedAccount(){
        //given
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        accountRepository.save(account);
        //when
        Account authenticatedAccount = accountService.getCurrentAuthenticatedAccount();
        //then
        assertThat(authenticatedAccount.getEmail()).isEqualTo(account.getEmail());
    }
}