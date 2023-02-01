package ua.shamray.myblogspringbootv1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Role;
import ua.shamray.myblogspringbootv1.repository.RoleRepository;
import ua.shamray.myblogspringbootv1.service.RoleService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Account setRoleAsUser(Account account) {
        Optional<Role> roleUserOptional = roleRepository.findById("ROLE_USER");
        Role roleUser = roleUserOptional.orElseThrow(() -> new NoSuchElementException("Role User not found"));
        account.getRoles().add(roleUser);
        return account;
    }

    @Override
    public Account setRoleAsAdmin(Account account) {
        Optional<Role> roleUserOptional = roleRepository.findById("ROLE_ADMIN");
        Role roleAdmin = roleUserOptional.orElseThrow(() -> new NoSuchElementException("Role User not found"));
        account.getRoles().add(roleAdmin);
        return account;
    }
}
