package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> getAll();
    Account save(Account account);
    Account saveNewUser(Account account);
    Account setUserAsAdmin(Account account);
    Optional<Account> getById(Long id);
    AccountDTO toDto(Account account);
    Boolean accountExists(Long id);
    Boolean accountExists(String email);
    Optional<Account> findByEmail(String email);

}
