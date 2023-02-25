package ua.shamray.myblogspringbootv1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.security.MyUser;
import ua.shamray.myblogspringbootv1.service.AuthenticationService;
import ua.shamray.myblogspringbootv1.service.PostService;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/posts")
public class PostController {
    private final PostService postService;
    private final AuthenticationService authenticationService;

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts(){
        return ResponseEntity.ok(postService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id){
      return ResponseEntity.ok(postService.getDTOById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PostDTO>> getAllUserPosts(Authentication authentication){
        MyUser principal = (MyUser) authentication.getPrincipal();
        long userID = principal.getUserID();
        List<PostDTO> userPosts = postService.getAllUserPostsByUserID(userID);
        return ResponseEntity.ok(userPosts);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createPost(@Valid @RequestBody PostDTO postDTO,
                              Principal principal) {
        Account account = authenticationService.getAuthenticatedUserByEmail(principal.getName());
        return postService.saveNewPost(postDTO, account);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @authenticationServiceImpl.isAuthenticatedUserAuthorOfPost(#id)")
    public ResponseEntity<PostDTO> editPost(@PathVariable Long id,
                                            @Valid @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postDTO));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || @authenticationServiceImpl.isAuthenticatedUserAuthorOfPost(#id)")
    public ResponseEntity<List<PostDTO>> deletePostById(@PathVariable Long id) {
        postService.deleteById(id);
        return getAllPosts();
    }

}
