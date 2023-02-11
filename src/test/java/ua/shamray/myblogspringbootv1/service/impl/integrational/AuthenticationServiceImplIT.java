package ua.shamray.myblogspringbootv1.service.impl.integrational;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import ua.shamray.myblogspringbootv1.dto.AccountDTO;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;
import ua.shamray.myblogspringbootv1.service.impl.AuthenticationServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "command.line.runner.enabled=true")
class AuthenticationServiceImplIT {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    @WithMockUser(username="findMe@mail.com", roles={"USER"})
    void isAuthenticatedUserAuthorOfPost() {
        //given
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        Account anotherAccount = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("another@mail.com")
                .build();
        Post post = Post.builder()
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        Post anotherPost = Post.builder()
                .account(anotherAccount)
                .body("postBody")
                .title("postTitle")
                .build();
        accountRepository.save(account);
        accountRepository.save(anotherAccount);
        postRepository.save(post);
        postRepository.save(anotherPost);
        //when
        boolean mustBeTrue = authenticationService.isAuthenticatedUserAuthorOfPost(post.getId());
        boolean mustBeFalse = authenticationService.isAuthenticatedUserAuthorOfPost(anotherPost.getId());
        //then
        assertThat(mustBeTrue).isTrue();
        assertThat(mustBeFalse).isFalse();
    }
}