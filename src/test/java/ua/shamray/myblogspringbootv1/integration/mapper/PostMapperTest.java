package ua.shamray.myblogspringbootv1.integration.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.mapper.PostMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostMapperTest {
    @Autowired
    private PostMapper postMapper;
    private Post post;
    private PostDTO postDTO;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        AccountViewer accountViewer = AccountViewer.builder()
                .email(account.getEmail())
                .build();
        post = Post.builder()
                .id(1L)
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        postDTO = PostDTO.builder()
                .id(1L)
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
    }

    @Test
    void entityToDTO() {
        //given
        PostDTO expected = postDTO;
        //when
        PostDTO resultPostDTO = postMapper.entityToDTO(post);
        //then
        assertThat(resultPostDTO.getId()).isEqualTo(expected.getId());
        assertThat(resultPostDTO.getAccountViewer().getEmail())
                .isEqualTo(expected.getAccountViewer().getEmail());
        assertThat(resultPostDTO.getTitle()).isEqualTo(expected.getTitle());
        assertThat(resultPostDTO.getBody()).isEqualTo(expected.getBody());
    }

    @Test
    void dtoToEntity() {
        //given
        Post expected = post;
        //when
        Post resultPost = postMapper.dtoToEntity(postDTO);
        //then
        assertNull(resultPost.getId());
        assertNull(resultPost.getAccount());
        assertEquals(resultPost.getTitle(), expected.getTitle());
        assertEquals(resultPost.getBody(), expected.getBody());
    }

    @Test
    void mapEntityWithDTO() {
        //given
        PostDTO newPostDTO = PostDTO.builder()
                .title("updatedTitle")
                .body("updatedBody")
                .build();

        //when
        Post resultPost = postMapper.mapEntityWithDTO(post, newPostDTO);

        //then
        assertEquals(resultPost.getTitle(), newPostDTO.getTitle());
        assertEquals(resultPost.getBody(), newPostDTO.getBody());
        assertEquals(resultPost.getId(), post.getId());
        assertEquals(resultPost.getAccount(), post.getAccount());
    }

}