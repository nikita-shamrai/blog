package ua.shamray.myblogspringbootv1.integration.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.repository.AccountRepository;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.impl.AuthenticationServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthenticationServiceImplITTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthenticationServiceImpl authenticationService;
    private Post post;

    @BeforeEach
    void setUp() {
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
        post = Post.builder()
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
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    @WithMockUser(username="findMe@mail.com")
    void isAuthenticatedUserAuthorOfPost_checkIfAnotherUserIsNotAuthorOfPost() {
        //when
        boolean authenticatedUserIsAuthorOfPost = authenticationService.isAuthenticatedUserAuthorOfPost(post.getId());
        //then
        assertThat(authenticatedUserIsAuthorOfPost).isTrue();
    }

    @Test
    @WithMockUser(username="another@mail.com")
    void isAuthenticatedUserAuthorOfPost_ThrowsAccessDeniedException_ForNonAuthor() {
        //when & then
        assertThrows(AccessDeniedException.class, () -> authenticationService.isAuthenticatedUserAuthorOfPost(post.getId()));
    }

    @Test
    @WithMockUser(roles = "ANONYMOUS")
    void isAuthenticatedUserAuthorOfPost_ThrowsSecurityException_ForUnauthenticatedUser() {
        //when & then
        assertThrows(SecurityException.class, () -> authenticationService.isAuthenticatedUserAuthorOfPost(post.getId()));
    }

}