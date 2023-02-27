package ua.shamray.myblogspringbootv1.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.mapper.AccountMapper;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.RoleService;

import java.util.List;
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
    public AccountDTO saveNewUser(AccountDTO accountDTO) {
        accountRepository
                .findByEmail(accountDTO.getEmail())
                .ifPresent(account -> {
                    throw new EntityExistsException(
                            String.format("Account with email %s already exists.", accountDTO.getEmail()));
                });
        Account account = accountMapper.dtoToEntity(accountDTO);
        String encodedPass = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPass);
        roleService.setRoleAsUser(account);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.entityToDTO(savedAccount);
    }

    @Override
    public void setUserAsAdmin(Account account) {
        accountRepository
                .findById(account.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        roleService.setRoleAsAdmin(account);
        accountRepository.save(account);
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


}
