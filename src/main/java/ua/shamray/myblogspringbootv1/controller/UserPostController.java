package ua.shamray.myblogspringbootv1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String usernameAuthenticated = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postService.getById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id=" + id + " doesn't exists"));
        boolean userAuthenticatedIsPostAuthor = post.getAccount().getEmail().equals(usernameAuthenticated);
        if (!userAuthenticatedIsPostAuthor) {
            throw new AccessException("You are not author of post id=" + id);
        }
        return postService.updatePost(id, postDTO);
    }

    @DeleteMapping("delete/{id}")
    @Secured({"ROLE_ADMIN"})
    public List<PostDTO> deletePostById(@PathVariable Long id){
        //NO CHECK IF ACCOUNT EXISTS!
        Boolean postDeleteSuccess = postService.deleteById(id);
        if (!postDeleteSuccess) {
            throw new ResourceNotFoundException("Post with id=" + id + " not found.");
        }
        return getAllPosts();
    }


}
