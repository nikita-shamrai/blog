package ua.shamray.myblogspringbootv1.service;


import ua.shamray.myblogspringbootv1.model.Account;

public interface RoleService {

    Account setRoleAsUser(Account account);
    Account setRoleAsAdmin(Account account);

}
