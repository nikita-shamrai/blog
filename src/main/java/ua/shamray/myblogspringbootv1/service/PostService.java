package ua.shamray.myblogspringbootv1.service;

import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import java.util.List;

public interface PostService {
    List<PostDTO> getAll();
    PostDTO saveNewPost(PostDTO postDTO, Account account);
    PostDTO getDTOById(Long id);
    Post getById(Long id);
    PostDTO updatePost(Long id, PostDTO postDTO);
    void deleteById(Long id);
    List<PostDTO> getAllUserPostsByUserID(Long userID);

}
