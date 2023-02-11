package ua.shamray.myblogspringbootv1.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Role;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.repository.RoleRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ConditionalOnProperty(
        prefix = "command.line.runner",
        value = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final PostService postService;
    private final AccountService accountService;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        if(accountService.getAll().isEmpty()){

            Role user = new Role();
            user.setName("ROLE_USER");
            roleRepository.save(user);

            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);

            Set<Role> userRoles = new HashSet<>();
            roleRepository.findById("ROLE_USER").ifPresent(userRoles::add);

            Account userAccount1 = Account.builder()
                    .firstName("user1")
                    .lastName("user1")
                    .email("user1@mail.com")
                    .password("password")
                    .roles(userRoles)
                    .postList(new ArrayList<>())
                    .build();
            accountService.saveNewUser(userAccount1);

            Account userAccount2 = Account.builder()
                    .firstName("user2")
                    .lastName("user2")
                    .email("user2@mail.com")
                    .password("password")
                    .roles(userRoles)
                    .postList(new ArrayList<>())
                    .build();
            accountService.saveNewUser(userAccount2);

            Account adminAccount = Account.builder()
                    .firstName("admin1")
                    .lastName("admin1")
                    .email("admin1@mail.com")
                    .password("password")
                    .roles(userRoles)
                    .postList(new ArrayList<>())
                    .build();
            accountService.saveNewUser(adminAccount);
            accountService.setUserAsAdmin(adminAccount);

            Post post1 = Post.builder()
                   .title("post1 titile")
                   .body("Body of post1")
                   .account(userAccount1)
                   .build();
            postService.save(post1);

            Post post2 = Post.builder()
                    .title("post2 titile")
                    .body("Body of post2")
                    .account(userAccount2)
                    .build();
            postService.save(post2);

        }
    }
}
