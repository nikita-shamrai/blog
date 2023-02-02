package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> getAll();
    Account saveNewUser(Account account);
    AccountDTO saveNewUser(AccountDTO accountDTO);
    void setUserAsAdmin(Account account);
    Boolean accountExists(String email);
    Optional<Account> findByEmail(String email);
    Account getCurrentAuthenticatedAccount();
    Account dtoToEntity(AccountDTO accountDTO);
    AccountDTO entityToDTO(Account account);


}
