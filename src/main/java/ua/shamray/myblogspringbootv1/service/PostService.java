package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> getAll();

    PostDTO saveNewPost(PostDTO postDTO);

    Post save(Post post);

    PostDTO getDTOById(Long id);

    Optional<Post> getById(Long id);

    PostDTO entityToDTO(Post post);

    Post dtoToEntity(PostDTO postDTO);

    PostDTO updatePost(Long id, PostDTO postDTO);

    void deleteById(Long id);

}
