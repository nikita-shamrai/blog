package ua.shamray.myblogspringbootv1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.MyUserDetailsService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountService.findByEmail(username);
        Account account = optionalAccount.orElseThrow(() -> {
            throw new UsernameNotFoundException("Email " + username + " not found.");
        });
        List<GrantedAuthority> grantedAuthorities = account
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }

}
