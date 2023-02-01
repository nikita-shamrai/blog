package ua.shamray.myblogspringbootv1.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Secured({"ROLE_ADMIN"})
public class AdminController {

    private final PostService postService;
    private final AccountService accountService;

    @PutMapping("/edit/{id}")
    public PostDTO editPost(@PathVariable Long id, @RequestBody PostDTO postDTO){
        return postService.updatePost(id, postDTO);
    }




}
