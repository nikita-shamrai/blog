package ua.shamray.myblogspringbootv1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.mapper.PostMapper;
import ua.shamray.myblogspringbootv1.model.Account;
import ua.shamray.myblogspringbootv1.model.Post;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.PostService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "command.line.runner.enabled=false")
class PostServiceImpUnitTest {

    @MockBean
    private PostRepository postRepository;
    @SpyBean
    private PostMapper postMapper;
    @MockBean
    private AccountService accountService;
    private PostService postService;
    private Account account;
    private AccountViewer accountViewer;
    private Post post1;
    private Post post2;
    private PostDTO post1DTO;
    private PostDTO post2DTO;


    @BeforeEach
    void setUp() {
        postService = new PostServiceImpl(postRepository,
                                          postMapper,
                                          accountService);
        account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        accountViewer = AccountViewer.builder()
                .email(account.getEmail())
                .build();
        post1 = Post.builder()
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        post2 = Post.builder()
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        post1DTO = PostDTO.builder()
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
        post2DTO = PostDTO.builder()
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
    }

    @Test
    void canGetAllPostsAndReturnListOfDTOs() {
        //given
        List<Post> postList = Arrays.asList(post1, post2);
        List<PostDTO> expectedDTOList = Arrays.asList(post1DTO, post2DTO);
        //when
        when(postRepository.findAll()).thenReturn(postList);
        List<PostDTO> resultDTOList = postService.getAll();
        //then
        for (int i = 0; i < expectedDTOList.size(); i++) {
            PostDTO postDTOFromExpected = expectedDTOList.get(i);
            PostDTO postDTOFromResult = resultDTOList.get(i);
            assertEquals(postDTOFromExpected.getAccountViewer().getEmail(),
                    postDTOFromResult.getAccountViewer().getEmail());
            assertEquals(postDTOFromExpected.getBody(),
                    postDTOFromResult.getBody());
            assertEquals(postDTOFromExpected.getTitle(),
                    postDTOFromResult.getTitle());
        }
    }

    @Test
    void canConvertPostEntityToDTO() {
        //given
        post1.setId(1L);
        post1DTO.setId(1L);
        Post subject = post1;
        PostDTO expected = post1DTO;
        //when
        PostDTO resultPostDTO = postService.entityToDTO(subject);
        //then
        assertThat(resultPostDTO.getId()).isEqualTo(expected.getId());
        assertThat(resultPostDTO.getAccountViewer().getEmail())
                .isEqualTo(expected.getAccountViewer().getEmail());
        assertThat(resultPostDTO.getTitle()).isEqualTo(expected.getTitle());
        assertThat(resultPostDTO.getBody()).isEqualTo(expected.getBody());
    }

    @Test
    void canConvertPostDTOToEntityIgnoringIdAndAccountFields() {
        //given
        post1.setId(1L);
        post1DTO.setId(1L);
        PostDTO subject = post1DTO;
        //fields id and account in expected must be null
        Post expected = post1;
        //when
        Post resultPost = postService.dtoToEntity(subject);
        //then
        assertNull(resultPost.getId());
        assertNull(resultPost.getAccount());
        assertEquals(resultPost.getTitle(), expected.getTitle());
        assertEquals(resultPost.getBody(), expected.getBody());
    }

    @Test
    void canUpdateExistingPostWithNewDataFromPostDTO() {
        //given
        post1.setId(1L);
        Post postToUpdate = post1;
        PostDTO argumentDTO = PostDTO.builder()
                .title("NewTitle")
                .body("NewBody")
                .build();
        //when
        when(postRepository.findById(postToUpdate.getId()))
                .thenReturn(Optional.of(postToUpdate));
        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(postService.entityToDTO(any(Post.class))).thenCallRealMethod();
        PostDTO resultDTO = postService.updatePost(1L, argumentDTO);
        //then
        assertEquals(resultDTO.getTitle(), argumentDTO.getTitle());
        assertEquals(resultDTO.getBody(), argumentDTO.getBody());
        assertEquals(resultDTO.getId(), postToUpdate.getId());
        assertEquals(resultDTO.getAccountViewer().getEmail(),
                    postToUpdate.getAccount().getEmail());
    }

    @Test
    void canDeletePostById() {
        //given
        post1.setId(1L);
        //when
        postService.deleteById(post1.getId());
        //then
        verify(postRepository).deleteById(1L);
    }

    @Test
    //getDTOById
    void canGetConvertedPostDTOFromPostEntityByEntityId() {
        //given
        post1.setId(1L);
        Post subjectPost = post1;
        //when
        when(postRepository.findById(1L))
                .thenReturn(Optional.ofNullable(subjectPost));
        when(postService.entityToDTO(subjectPost)).thenCallRealMethod();
        PostDTO resultDTO = postService.getDTOById(post1.getId());
        //then
        assertEquals(resultDTO.getId(), subjectPost.getId());
        assertEquals(resultDTO.getAccountViewer().getEmail(),
                subjectPost.getAccount().getEmail());
        assertEquals(resultDTO.getTitle(), subjectPost.getTitle());
        assertEquals(resultDTO.getBody(), subjectPost.getBody());
    }

    @Test
    void canGetPostById() {
        //given
        post1.setId(1L);
        //when
        postService.getById(post1.getId());
        //then
        verify(postRepository).findById(1L);
    }

    @Test
    void saveNewPost() {

    }

    @Test
    void canSavePostIfEntityProvided() {
        //when
        postService.save(post1);
        //then
        verify(postRepository).save(post1);
    }
}