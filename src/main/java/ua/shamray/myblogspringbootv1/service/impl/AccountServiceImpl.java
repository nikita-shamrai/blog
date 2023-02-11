package ua.shamray.myblogspringbootv1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.mapper.AccountMapper;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.RoleService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account saveNewUser(Account account) {
        String encodedPass = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPass);
        if(Objects.isNull(account.getRoles())) {
            roleService.setRoleAsUser(account);
        }
        return accountRepository.save(account);
    }

    @Override
    public AccountDTO saveNewUser(AccountDTO accountDTO) throws IllegalArgumentException {
        if (accountExists(accountDTO.getEmail())) {
            throw new IllegalArgumentException("Account with email " + accountDTO.getEmail() + " already exists.");
        }
        Account account = dtoToEntity(accountDTO);
        Account savedAccount = saveNewUser(account);
        return entityToDTO(savedAccount);
    }

    @Override
    public void setUserAsAdmin(Account account) {
        if (accountRepository.findById(account.getId()).isEmpty()){
            throw new IllegalArgumentException("User not found.");
        }
        roleService.setRoleAsAdmin(account);
        accountRepository.save(account);
    }

    @Override
    public Boolean accountExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Account getCurrentAuthenticatedAccount() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByEmail(userName).orElseThrow(() -> new SecurityException(
                "SecurityContextHolder getName error. Check user authentication."));
    }

    @Override
    public Account dtoToEntity(AccountDTO accountDTO) {
        return accountMapper.dtoToEntity(accountDTO);
    }
    @Override
    public AccountDTO entityToDTO(Account account) {
        return accountMapper.entityToDTO(account);
    }

}
