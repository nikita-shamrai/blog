package ua.shamray.myblogspringbootv1.unit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.impl.UserDetailsServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplUnitTest {

    @Mock
    private AccountService accountService;
    @InjectMocks
    private UserDetailsServiceImpl myUserDetailsService;

    @Test
    void loadUserByUsername_canCreateNewSecurityUserByUsername() {
        //given
        Account account = Account.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(Set.of(Role.builder().roleType(RoleType.ROLE_USER).build()))
                .build();
        UserDetails userDetails = User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
        when(accountService.findByEmail(account.getEmail())).thenReturn(Optional.of(account));

        //when
        UserDetails loadUserByUsername = myUserDetailsService.loadUserByUsername(account.getEmail());

        //then
        assertThat(loadUserByUsername).isEqualTo(userDetails);
    }

    @Test
    public void loadUserByUsername_throwsEntityNotFoundException() {
        // given
        when(accountService.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername("test"));
    }

}