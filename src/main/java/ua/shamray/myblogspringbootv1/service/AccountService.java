package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountDTO saveNewUser(AccountDTO accountDTO);
    void setUserAsAdmin(Account account);
    Optional<Account> findByEmail(String email);
    Account getCurrentAuthenticatedAccount();

}
