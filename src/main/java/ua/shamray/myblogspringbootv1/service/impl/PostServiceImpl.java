package ua.shamray.myblogspringbootv1.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.mapper.PostMapper;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.PostService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public List<PostDTO> getAll(){
        return postRepository.findAll().stream().map(postMapper::entityToDTO).toList();
    }

    @Override
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Post with id=%s doesn't exist", id)));
        Post updatedPost = postMapper.mapEntityWithDTO(post, postDTO);
        return postMapper.entityToDTO(postRepository.save(updatedPost));
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<PostDTO> getAllUserPostsByUserID(Long userID) {
        return postRepository
                .findPostsByAccountID(userID)
                .stream()
                .map(postMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getDTOById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return postMapper.entityToDTO(post.orElseThrow(
                () -> new EntityNotFoundException(String.format("Post with id=%s doesn't exist", id))));
    }

    @Override
    public Post getById(Long id) {
        return postRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Post with id=%s doesn't exist", id)));
    }

    @Override
    public PostDTO saveNewPost(PostDTO postDTO, Account account){
        Post post = postMapper.dtoToEntity(postDTO);
        post.setAccount(account);
        return postMapper.entityToDTO(postRepository.save(post));
    }

}
