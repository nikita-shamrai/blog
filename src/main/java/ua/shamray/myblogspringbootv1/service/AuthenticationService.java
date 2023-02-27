package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.entity.Account;

public interface AuthenticationService {
    boolean isAuthenticatedUserAuthorOfPost(Long postId);
    Account getAuthenticatedUserByEmail(String email);

}
