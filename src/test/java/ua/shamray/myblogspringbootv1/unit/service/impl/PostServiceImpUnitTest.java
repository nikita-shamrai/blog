package ua.shamray.myblogspringbootv1.unit.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.mapper.PostMapper;
import ua.shamray.myblogspringbootv1.repository.PostRepository;
import ua.shamray.myblogspringbootv1.service.impl.PostServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImpUnitTest {
    @Mock
    private PostRepository postRepository;
    @Spy
    private PostMapper postMapper;
    @InjectMocks
    private PostServiceImpl postService;
    private Post post1;
    private Post post2;
    private PostDTO post1DTO;
    private PostDTO post2DTO;
    private  Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .build();
        AccountViewer accountViewer = AccountViewer.builder()
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
    void getAll_canGetAllPostsAndReturnListOfDTOs() {
        //given
        List<Post> postList = Arrays.asList(post1, post2);
        List<PostDTO> expectedDTOList = Arrays.asList(post1DTO, post2DTO);

        when(postRepository.findAll()).thenReturn(postList);
        when(postMapper.entityToDTO(post1)).thenReturn(post1DTO);
        when(postMapper.entityToDTO(post2)).thenReturn(post2DTO);

        //when
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
    void updatePost_canUpdateExistingPostWithNewDataFromPostDTO() {
        //given
        post1.setId(1L);
        Post postToUpdate = post1;
        PostDTO argumentDTO = PostDTO.builder()
                .title("NewTitle")
                .body("NewBody")
                .build();
        PostDTO expected = PostDTO.builder()
                .title(argumentDTO.getTitle())
                .body(argumentDTO.getBody())
                .id(postToUpdate.getId())
                .accountViewer(AccountViewer.builder()
                        .email(post1.getAccount().getEmail())
                        .build())
                .build();
        when(postRepository.findById(postToUpdate.getId()))
                .thenReturn(Optional.of(postToUpdate));
        when(postRepository.save(any()))
                .thenReturn(post1);
        when(postMapper.entityToDTO(post1)).thenReturn(expected);

        //when
        PostDTO resultDTO = postService.updatePost(1L, argumentDTO);

        //then
        assertEquals(resultDTO.getTitle(), argumentDTO.getTitle());
        assertEquals(resultDTO.getBody(), argumentDTO.getBody());
        assertEquals(resultDTO.getId(), postToUpdate.getId());
        assertEquals(resultDTO.getAccountViewer().getEmail(),
                    postToUpdate.getAccount().getEmail());
    }

    @Test
    public void updatePost_testUpdatePostWithInvalidId() {
        // given
        Long id = 1L;
        PostDTO postDTO = new PostDTO();
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> postService.updatePost(id, postDTO));

        verify(postRepository).findById(id);
        verifyNoMoreInteractions(postRepository);
        verifyNoInteractions(postMapper);
    }

    @Test
    void deleteById_canDeletePostById() {
        //given
        post1.setId(1L);

        //when
        postService.deleteById(post1.getId());

        //then
        verify(postRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void getAllUserPostsByUserID_shouldReturnListOfPostDTOs() {
        // given
        Long userId = 1L;
        List<Post> posts = List.of(post1, post2);
        Mockito.when(postRepository.findPostsByAccountID(userId)).thenReturn(posts);

        List<PostDTO> postDTOs = List.of(post1DTO, post2DTO);
        Mockito.when(postMapper.entityToDTO(post1)).thenReturn(post1DTO);
        Mockito.when(postMapper.entityToDTO(post2)).thenReturn(post2DTO);

        // when
        List<PostDTO> result = postService.getAllUserPostsByUserID(userId);

        // then
        Assertions.assertEquals(postDTOs, result);
    }

    @Test
    public void getAllUserPostsByUserID_shouldReturnEmptyList_whenNoPostsFound() {
        // given
        Long userId = 1L;
        Mockito.when(postRepository.findPostsByAccountID(userId)).thenReturn(new ArrayList<>());

        // when
        List<PostDTO> result = postService.getAllUserPostsByUserID(userId);

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getDTOById_canGetPostDTOFromPostEntityById() {
        // given
        post1.setId(1L);
        post1DTO.setId(1L);
        when(postRepository.findById(1L))
                .thenReturn(Optional.ofNullable(post1));
        when(postMapper.entityToDTO(post1)).thenReturn(post1DTO);

        // when
        PostDTO resultDTO = postService.getDTOById(post1.getId());

        // then
        assertEquals(resultDTO.getId(), post1.getId());
        assertEquals(resultDTO.getAccountViewer().getEmail(),
                post1.getAccount().getEmail());
        assertEquals(resultDTO.getTitle(), post1.getTitle());
        assertEquals(resultDTO.getBody(), post1.getBody());
    }

    @Test
    public void getById_shouldReturnPost_whenPostExists() {
        // given
        post1.setId(1L);
        Mockito.when(postRepository.findById(post1.getId())).thenReturn(Optional.of(post1));

        // when
        Post result = postService.getById(post1.getId());

        // then
        Assertions.assertEquals(post1, result);
    }

    @Test
    public void getById_shouldThrowException_whenPostDoesNotExist() {
        // given
        post1.setId(1L);
        Mockito.when(postRepository.findById(post1.getId())).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(EntityNotFoundException.class,
                () ->
                postService.getById(post1.getId()));
    }

    @Test
    public void saveNewPost_shouldReturnPostDTO() {
        // given
        post1.setId(1L);
        Mockito.when(postMapper.dtoToEntity(post1DTO)).thenReturn(post1);
        Mockito.when(postRepository.save(post1)).thenReturn(post1);
        PostDTO expectedPostDTO = new PostDTO();
        Mockito.when(postMapper.entityToDTO(post1)).thenReturn(expectedPostDTO);

        // when
        PostDTO result = postService.saveNewPost(post1DTO, account);

        // then
        Assertions.assertEquals(expectedPostDTO, result);
    }

}