package ua.shamray.myblogspringbootv1.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.AuthenticationService;
import ua.shamray.myblogspringbootv1.service.PostService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountService accountService;
    private final PostService postService;

    @Override
    public boolean isAuthenticatedUserAuthorOfPost(Long postId) {
        String usernameAuthenticated = accountService.getCurrentAuthenticatedAccount().getEmail();
        Post post = postService.getById(postId);
        if (!post.getAccount().getEmail().equals(usernameAuthenticated)) {
            throw new AccessDeniedException(String.format("You are not author of post id=%s", postId));
        }
        return true;
    }

    @Override
    public Account getAuthenticatedUserByEmail(String email) {
        return accountService
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Account with email: %s doesn't exist", email)));
    }

}
