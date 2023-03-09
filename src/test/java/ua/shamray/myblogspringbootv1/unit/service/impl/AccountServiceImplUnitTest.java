package ua.shamray.myblogspringbootv1.unit.service.impl;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.mapper.AccountMapper;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.service.RoleService;
import ua.shamray.myblogspringbootv1.service.impl.AccountServiceImpl;

import java.util.Optional;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class )
class AccountServiceImplUnitTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleService roleService;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Spy
    private AccountMapper accountMapper;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void saveNewUser_shouldReturnSavedAccount() {
        // given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail("test@test.com");
        accountDTO.setPassword("password");

        Account account = new Account();
        account.setEmail("test@test.com");
        account.setPassword("password");

        Mockito.when(accountRepository.findByEmail(accountDTO.getEmail())).thenReturn(Optional.empty());
        Mockito.when(accountMapper.dtoToEntity(accountDTO)).thenReturn(account);
        Mockito.when(passwordEncoder.encode(account.getPassword())).thenReturn("encodedPassword");
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.when(accountMapper.entityToDTO(account)).thenReturn(accountDTO);

        // when
        AccountDTO savedAccountDTO = accountService.saveNewUser(accountDTO);

        // then
        verify(roleService).setRoleAsUser(account);
        Assertions.assertEquals(accountDTO, savedAccountDTO);
    }

    @Test
    public void saveNewUser_shouldThrowEntityExistsException() {
        // given
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail("test@test.com");
        accountDTO.setPassword("password");

        Account account = new Account();
        account.setEmail("test@test.com");
        account.setPassword("password");

        Mockito.when(accountRepository.findByEmail(accountDTO.getEmail())).thenReturn(Optional.of(account));

        // when & then
        Assertions.assertThrows(EntityExistsException.class, () -> accountService.saveNewUser(accountDTO));
    }


    @Test
    public void findByEmail_shouldReturnEmptyOptional_whenNoAccountExists() {
        // given
        String email = "test@test.com";
        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        Optional<Account> accountOptional = accountService.findByEmail(email);

        // then
        Assertions.assertTrue(accountOptional.isEmpty());
    }

    @Test
    public void findByEmail_shouldReturnNonEmptyOptional_whenAccountExists() {
        // given
        String email = "test@test.com";
        Account account = new Account();
        account.setEmail(email);

        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));

        // when
        Optional<Account> accountOptional = accountService.findByEmail(email);

        // then
        Assertions.assertTrue(accountOptional.isPresent());
        Assertions.assertEquals(email, accountOptional.get().getEmail());
    }

}