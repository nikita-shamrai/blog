package ua.shamray.myblogspringbootv1.service.impl.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.repository.RoleRepository;
import ua.shamray.myblogspringbootv1.service.RoleService;
import ua.shamray.myblogspringbootv1.service.impl.RoleServiceImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "command.line.runner.enabled=false")
class RoleServiceImplUnitTest {

    @MockBean
    private RoleRepository roleRepository;
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    @Disabled
    void setRoleAsUser() {
        //given
        Role userRole = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();

        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        Mockito.when(roleRepository.findById("ROLE_USER")).thenReturn(Optional.ofNullable(userRole));
        roleService.setRoleAsUser(account);
        //then
        assertThat(account.getRoles()).isNotEmpty();
    }

    @Test
    @Disabled
    void setRoleAsAdminThrowsExIfUserIsNotRegistered() {
        //given
        Role adminRole = Role.builder()
                .roleType(RoleType.ROLE_ADMIN)
                .build();

        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        //when
        Mockito.when(roleRepository.findById("ROLE_ADMIN")).thenReturn(Optional.ofNullable(adminRole));
        //then
        assertThrows(IllegalArgumentException.class, () -> roleService.setRoleAsAdmin(account));
    }

    @Test
    @Disabled
    void canSetRoleAsAdmin() {
        //given
        Role userRole = Role.builder()
                .roleType(RoleType.ROLE_USER)
                .build();
        Role adminRole = Role.builder()
                .roleType(RoleType.ROLE_ADMIN)
                .build();

        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(new HashSet<>(Arrays.asList(userRole)))
                .build();
        //when
        Mockito.when(roleRepository.findById("ROLE_ADMIN")).thenReturn(Optional.ofNullable(adminRole));
        roleService.setRoleAsAdmin(account);
        //then
        assertThat(account.getRoles().size()).isGreaterThan(1);
    }
}