package ua.shamray.myblogspringbootv1.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
@Getter
public class MyUser extends User {
    private final long userID;
    @Builder(builderMethodName = "myBuilder")
    public MyUser(String username,
                  String password,
                  Collection<? extends GrantedAuthority> authorities,
                  long userID) {
        super(username, password, authorities);
        this.userID = userID;
    }

}
