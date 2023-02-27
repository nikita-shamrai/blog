package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.entity.Account;

public interface RoleService {
    void setRoleAsUser(Account account);
    void setRoleAsAdmin(Account account);

}
