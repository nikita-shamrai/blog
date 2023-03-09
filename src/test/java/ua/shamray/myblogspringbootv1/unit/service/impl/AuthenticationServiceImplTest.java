package ua.shamray.myblogspringbootv1.unit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;
import ua.shamray.myblogspringbootv1.service.impl.AuthenticationServiceImpl;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @Mock
    private AccountService accountService;
    @Mock
    private PostService postService;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    private Account account1;
    private Post post1;
    private Post post2;

    @BeforeEach
    void setUp() {
        account1 = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(Set.of(Role
                        .builder()
                        .roleType(RoleType.ROLE_USER)
                        .build()))
                .build();

        Account account2 = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("another@mail.com")
                .roles(Set.of(Role
                        .builder()
                        .roleType(RoleType.ROLE_USER)
                        .build()))
                .build();
        post1 = Post.builder()
                .id(1L)
                .account(account1)
                .body("postBody")
                .title("postTitle")
                .build();
        post2 = Post.builder()
                .id(2L)
                .account(account2)
                .body("postBody")
                .title("postTitle")
                .build();
    }

    @Test
    public void isAuthenticatedUserAuthorOfPost_shouldThrowAccessDeniedException() {
        // given
        when(accountService.getCurrentAuthenticatedAccount()).thenReturn(account1);
        when(postService.getById(post2.getId())).thenReturn(post2);

        // when & then
        assertThrows(AccessDeniedException.class,
                () -> authenticationService.isAuthenticatedUserAuthorOfPost(post2.getId()));
    }

   @Test
    public void isAuthenticatedUserAuthorOfPost_shouldReturnTrue() {
        // given
        Long postId = 1L;
        when(accountService.getCurrentAuthenticatedAccount()).thenReturn(account1);
        when(postService.getById(postId)).thenReturn(post1);

        // when
        boolean result = authenticationService.isAuthenticatedUserAuthorOfPost(postId);

        // then
        assertTrue(result);
    }

   @Test
    public void getAuthenticatedUserByEmail_shouldReturnAccount() {
        // given
        String email = "findMe@mail.com";
        when(accountService.findByEmail(email)).thenReturn(Optional.of(account1));

        // when
        Account result = authenticationService.getAuthenticatedUserByEmail(email);

        // then
        assertNotNull(result);
        assertEquals(account1, result);
    }

  @Test
    public void getAuthenticatedUserByEmail_shouldThrowEntityNotFoundException() {
        // given
        String email = "findMe@mail.com";
        when(accountService.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class,
              () -> authenticationService.getAuthenticatedUserByEmail(email));
    }

}