package ua.shamray.myblogspringbootv1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.exception.ResourceNotFoundException;
import ua.shamray.myblogspringbootv1.model.Post;
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
        Post post = postService.getById(postId).orElseThrow(() -> new ResourceNotFoundException("Post with postId=" + postId + " doesn't exist"));
        return post.getAccount().getEmail().equals(usernameAuthenticated);
    }

}
