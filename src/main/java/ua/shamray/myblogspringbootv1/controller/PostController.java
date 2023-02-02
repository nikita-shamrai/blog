package ua.shamray.myblogspringbootv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.exception.ResourceNotFoundException;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.AuthenticationService;
import ua.shamray.myblogspringbootv1.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/posts")
public class PostController {
    private final PostService postService;
    private final AccountService accountService;
    private final AuthenticationService authenticationService;

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id){
      return ResponseEntity.ok(postService.getDTOById(id));
    }

    //is it Ok to map entity to dto here? If no - where?
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostDTO>> getAllUserPosts(){
        List<PostDTO> userPosts = accountService
                .getCurrentAuthenticatedAccount()
                .getPostList()
                .stream()
                .map(postService::entityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userPosts);
    }

    //TODO: Разобраться с РеспонсЭнтити. Сделать логин и регистрацию. Разобраться с отображаемым контентом пользователю. Тестирование.
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createPost(@Valid @RequestBody PostDTO postDTO) {
        return postService.saveNewPost(postDTO);
    }

    //is it Ok or can I optimize this?
    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostDTO> editPost(@PathVariable Long id,
                                            @Valid @RequestBody PostDTO postDTO,
                                            HttpServletRequest request) throws AccessException {
        if(postService.getById(id).isEmpty()){
            throw new ResourceNotFoundException("Post with postId=" + id + " doesn't exist");
        }
        if (request.isUserInRole("ROLE_ADMIN")){
            return ResponseEntity.ok(postService.updatePost(id, postDTO));
        }
        if (!authenticationService.isAuthenticatedUserAuthorOfPost(id)) {
            throw new AccessException("You are not author of post id=" + id);
        }
        return ResponseEntity.ok(postService.updatePost(id, postDTO));
    }

    //is it Ok or can I optimize this?
    @DeleteMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostDTO>> deletePostById(@PathVariable Long id,
                                                        HttpServletRequest request) throws AccessException {
        if(postService.getById(id).isEmpty()){
            throw new ResourceNotFoundException("Post with postId=" + id + " doesn't exist");
        }
        if (request.isUserInRole("ROLE_ADMIN")){
            postService.deleteById(id);
            return getAllPosts();
        }
        if (!authenticationService.isAuthenticatedUserAuthorOfPost(id)) {
            throw new AccessException("You are not author of post id=" + id);
        }
        postService.deleteById(id);
        return getAllPosts();
    }

}
