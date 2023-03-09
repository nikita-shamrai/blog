package ua.shamray.myblogspringbootv1.unit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.repository.RoleRepository;
import ua.shamray.myblogspringbootv1.service.impl.RoleServiceImpl;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplUnitTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;
    private Role roleUser;
    private Role roleAdmin;
    private Account account;

    @BeforeEach
    void setUp() {
        roleUser = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        roleAdmin = Role.builder()
                .roleType(RoleType.ROLE_ADMIN)
                .build();
        account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(new HashSet<>())
                .build();
    }

    @Test
    void setRoleAsUser_shouldAddUserRoleToAnEmptyAccount() {
        //given
        Mockito.when(roleRepository.findByRoleType(RoleType.ROLE_USER))
                .thenReturn(Optional.ofNullable(roleUser));

        //when
        roleService.setRoleAsUser(account);

        //then
        assertTrue(account.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getRoleType().equals(RoleType.ROLE_USER)));
    }

    @Test
    void setRoleAsUser_shouldThrowEntityNotFoundExceptionWhenRoleNotFound() {
        //given
        Mockito.when(roleRepository.findByRoleType(RoleType.ROLE_USER))
                .thenReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class,
                () -> roleService.setRoleAsUser(account));
    }

    @Test
    void setRoleAsAdmin_shouldThrowEntityNotFoundExceptionWhenRoleNotFound() {
        //given
        Mockito.when(roleRepository.findByRoleType(RoleType.ROLE_ADMIN))
                .thenReturn(Optional.empty());

        //when & then
        assertThrows(EntityNotFoundException.class,
                () -> roleService.setRoleAsAdmin(account));
    }

    @Test
    void canSetRoleAsAdmin() {
        //given
        Mockito.when(roleRepository.findByRoleType(RoleType.ROLE_ADMIN))
                .thenReturn(Optional.ofNullable(roleAdmin));

        //when
        roleService.setRoleAsAdmin(account);

        //then
        assertTrue(account.getRoles()
                .stream()
                .anyMatch(role ->
                        role.getRoleType().equals(RoleType.ROLE_ADMIN)));
    }

}