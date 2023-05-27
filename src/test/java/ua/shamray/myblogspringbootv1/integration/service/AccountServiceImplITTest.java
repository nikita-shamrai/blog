package ua.shamray.myblogspringbootv1.integration.service;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceImplITTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountService accountService;

    @BeforeEach
    void beforeEach() {
        Role roleUser = Role.builder().roleType(RoleType.ROLE_USER).build();
        Role roleAdmin = Role.builder().roleType(RoleType.ROLE_ADMIN).build();
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void saveNewUser_shouldReturnSavedAccount() {
        // given
        AccountDTO accountDTO = AccountDTO.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        // when
        AccountDTO savedAccountDTO = accountService.saveNewUser(accountDTO);
        // then
        assertNotNull(savedAccountDTO.getId());
        assertEquals(accountDTO.getEmail(), savedAccountDTO.getEmail());
    }

    @Test
    void saveNewUser_shouldThrowEntityExistsException(){
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

        accountService.saveNewUser(accountDTO);
        //when & then
        assertThrows(EntityExistsException.class, () -> accountService.saveNewUser(accountDTOWithSameEmail));
    }

    @Test
    void setUserAsAdmin_shouldSetAdminRoleToExistingUser() {
        //given
        Role userRole = roleRepository.findByRoleType(RoleType.ROLE_USER).get();

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
        boolean accountHasAdminRole = account.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleType().equals(RoleType.ROLE_ADMIN));
        assertThat(accountHasAdminRole).isTrue();
    }

    @Test
    @WithMockUser(username = "findMe@mail.com", authorities = "ROLE_USER")
    void getCurrentAuthenticatedAccount_ReturnsAccount_ForAuthenticatedUser() {
        //given
        Role userRole = roleRepository.findByRoleType(RoleType.ROLE_USER).get();
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(new HashSet<>(List.of(userRole)))
                .build();
        accountRepository.save(account);
        //when
        Account result = accountService.getCurrentAuthenticatedAccount();
        //then
        assertNotNull(result);
        //the result should have the same email address as the authenticated user
        assertEquals("findMe@mail.com", result.getEmail());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ANONYMOUS")
    void getCurrentAuthenticatedAccount_ThrowsSecurityException_ForUnauthenticatedUser() {
        // when & then
        assertThrows(SecurityException.class, () -> accountService.getCurrentAuthenticatedAccount());
    }

}