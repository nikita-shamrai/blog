package ua.shamray.myblogspringbootv1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
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
    public List<PostDTO> getAllPosts(){
        return postService.getAll();
    }

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable Long id){
      return postService.getDTOById(id);
    }

    //is it Ok to map entity to dto here? If no - where?
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public List<PostDTO> getAllUserPosts(){
        return accountService
                .getCurrentAuthenticatedAccount()
                .getPostList()
                .stream()
                .map(postService::entityToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postService.saveNewPost(postDTO);
    }

    //is it Ok or can I optimize this?
    @PutMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public PostDTO editPost(@PathVariable Long id, @RequestBody PostDTO postDTO, HttpServletRequest request) throws AccessException {
        if(postService.getById(id).isEmpty()){
            throw new ResourceNotFoundException("Post with postId=" + id + " doesn't exist");
        }
        if (request.isUserInRole("ROLE_ADMIN")){
            return postService.updatePost(id, postDTO);
        }
        if (!authenticationService.isAuthenticatedUserAuthorOfPost(id)) {
            throw new AccessException("You are not author of post id=" + id);
        }
        return postService.updatePost(id, postDTO);
    }

    //is it Ok or can I optimize this?
    @DeleteMapping("delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public List<PostDTO> deletePostById(@PathVariable Long id, HttpServletRequest request) throws AccessException {
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
