package ua.shamray.myblogspringbootv1.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.exception.ResourceNotFoundException;
import ua.shamray.myblogspringbootv1.mapper.PostMapper;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AccountService accountService;

    @Override
    public List<PostDTO> getAll(){
        return postRepository.findAll().stream().map(this::entityToDTO).toList();
    }

    @Override
    public PostDTO entityToDTO(Post post) {
        return postMapper.entityToDTO(post);
    }
    @Override
    public Post dtoToEntity(PostDTO postDTO) {
        return postMapper.dtoToEntity(postDTO);
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post with id=" + id + " doesn't exists"));
        Post updatedPost = postMapper.mapEntityWithDTO(post, postDTO);
        return entityToDTO(postRepository.save(updatedPost));
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostDTO getDTOById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return entityToDTO(post.orElseThrow(() -> new ResourceNotFoundException("Post with id=" + id + " doesn't exists")));
    }

    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    //is it ok to inject accountService here?
    @Override
    public PostDTO saveNewPost(PostDTO postDTO){
        Post post = dtoToEntity(postDTO);
        post.setAccount(accountService.getCurrentAuthenticatedAccount());
        return entityToDTO(postRepository.save(post));
    }
    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

}
