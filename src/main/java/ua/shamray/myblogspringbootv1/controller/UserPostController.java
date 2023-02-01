package ua.shamray.myblogspringbootv1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.exception.ResourceNotFoundException;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/posts")
public class UserPostController {
    private final PostService postService;
    private final AccountService accountService;

    @GetMapping()
    public List<PostDTO> getAllPosts(){
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable Long id){
      return postService.getDTOById(id);
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.saveNewPost(postDTO);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public PostDTO editPost(@PathVariable Long id, @RequestBody PostDTO postDTO) throws AccessException {
        if (!isAuthenticatedUserAuthorOfPost(id)) {
            throw new AccessException("You are not author of post id=" + id);
        }
        return postService.updatePost(id, postDTO);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public List<PostDTO> deletePostById(@PathVariable Long id) throws AccessException {
        if (!isAuthenticatedUserAuthorOfPost(id)) {
            throw new AccessException("You are not author of post id=" + id);
        }
        postService.deleteById(id);
        return getAllPosts();
    }

    //TODO: Add show my posts

    //Is it Ok to extract private methods in controller?
    private boolean isAuthenticatedUserAuthorOfPost(Long postId) {
        String usernameAuthenticated = accountService.getCurrentAuthenticatedAccount().getEmail();
        Post post = postService.getById(postId).orElseThrow(() -> new ResourceNotFoundException("Post with postId=" + postId + " doesn't exists"));
        return post.getAccount().getEmail().equals(usernameAuthenticated);
    }


}
