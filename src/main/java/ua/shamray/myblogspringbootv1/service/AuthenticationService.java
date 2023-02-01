package ua.shamray.myblogspringbootv1.service;

public interface AuthenticationService {

    boolean isAuthenticatedUserAuthorOfPost(Long postId);

}
