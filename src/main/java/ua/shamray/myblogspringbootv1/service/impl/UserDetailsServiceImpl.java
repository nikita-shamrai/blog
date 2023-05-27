package ua.shamray.myblogspringbootv1.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.security.MyUser;
import ua.shamray.myblogspringbootv1.service.AccountService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountService.findByEmail(username);
        Account account = optionalAccount.orElseThrow(() -> new EntityNotFoundException(String.format("Email %s not found.", username)));
        List<GrantedAuthority> grantedAuthorities = account
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleType().name()))
                .collect(Collectors.toList());
        return MyUser.myBuilder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities(grantedAuthorities)
                .userID(account.getId())
                .build();
    }

}
