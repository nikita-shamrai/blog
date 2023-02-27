package ua.shamray.myblogspringbootv1.service.impl.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.impl.UserDetailsServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "command.line.runner.enabled=false")
class UserDetailsServiceImplUnitTest {

    @MockBean
    private AccountService accountService;
    private UserDetailsService myUserDetailsService;

    @BeforeEach
    void setUp() {
        myUserDetailsService = new UserDetailsServiceImpl(accountService);
    }

    //Do I need to test Throws in Optional?
    @Test
    @Disabled
    void canCreateNewSecurityUserByUsername() {
        //given
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(Set.of(Role.builder().name(RoleType.ROLE_USER).build()))
                .build();
        UserDetails userDetails = User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        //when
        when(accountService.findByEmail(account.getEmail())).thenReturn(Optional.of(account));
        UserDetails loadUserByUsername = myUserDetailsService.loadUserByUsername(account.getEmail());
        //then
        assertThat(loadUserByUsername).isEqualTo(userDetails);
    }

}