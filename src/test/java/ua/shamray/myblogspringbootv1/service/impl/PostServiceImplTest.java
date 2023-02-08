package ua.shamray.myblogspringbootv1.service.impl;

import org.junit.jupiter.api.Test;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {

    @Test
    void getAll() {
    }

    @Test
    void entityToDTO() {
    }

    @Test
    void dtoToEntity() {
        PostDTO postDTO = PostDTO.builder()
                .accountViewer(AccountViewer.builder()
                        .email("findMe@mail.com")
                        .build())
                .body("postBody")
                .title("postTitle")
                .build();
    }

    @Test
    void updatePost() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getDTOById() {
    }

    @Test
    void getById() {
    }

    @Test
    void saveNewPost() {
    }

    @Test
    void save() {
    }
}