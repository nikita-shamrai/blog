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
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if(Objects.isNull(account.getRoles())) {
            roleService.setRoleAsUser(account);
        }
        return accountRepository.save(account);
    }

    @Override
    public AccountDTO saveNewUser(AccountDTO accountDTO) {
        Account account = dtoToEntity(accountDTO);
        Account savedAccount = saveNewUser(account);
        return entityToDTO(savedAccount);
    }

    @Override
    public void setUserAsAdmin(Account account) {
        accountRepository
                .findById(account.getId())
                .ifPresentOrElse(
                        account1 -> {
                            roleService.setRoleAsAdmin(account1);
                            accountRepository.save(account1);
                        },
                        () -> {
                            throw new IllegalArgumentException("User not found.");
                        }
                );
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
