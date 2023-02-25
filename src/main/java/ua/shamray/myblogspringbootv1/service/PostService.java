package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> getAll();
    PostDTO saveNewPost(PostDTO postDTO, Account account);
    Post save(Post post);
    PostDTO getDTOById(Long id);
    Post getById(Long id);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deleteById(Long id);
    List<PostDTO> getAllUserPostsByUserID(Long userID);

}
