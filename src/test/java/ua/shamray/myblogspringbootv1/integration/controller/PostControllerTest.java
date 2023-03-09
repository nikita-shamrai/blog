package ua.shamray.myblogspringbootv1.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.shamray.myblogspringbootv1.controller.PostController;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Post;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.service.AuthenticationService;
import ua.shamray.myblogspringbootv1.service.PostService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
//testing with Spring Security OFF
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;
    @MockBean
    private AuthenticationService authenticationService;
    private PostDTO post1DTO;
    private List<PostDTO> postDTOList;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("findMe@mail.com")
                .roles(Set.of(Role
                        .builder()
                        .roleType(RoleType.ROLE_USER)
                        .build()))
                .build();
        AccountViewer accountViewer = AccountViewer.builder()
                .email(account.getEmail())
                .build();
        Post post1 = Post.builder()
                .id(1L)
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        Post post2 = Post.builder()
                .id(2L)
                .account(account)
                .body("postBody")
                .title("postTitle")
                .build();
        post1DTO = PostDTO.builder()
                .id(1L)
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
        PostDTO post2DTO = PostDTO.builder()
                .id(2L)
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
        postDTOList = Arrays.asList(post1DTO, post2DTO);
    }

    @Test
    void getAllPosts_shouldReturnPostDTOList() throws Exception {
        //given
        when(postService.getAll()).thenReturn(postDTOList);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/blog/posts"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseJson = mvcResult.getResponse().getContentAsString();
        List<PostDTO> resultList = objectMapper.readValue(responseJson, new TypeReference<>() {
        });
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i), postDTOList.get(i));
        }
    }

    @Test
    void getPostById() throws Exception {
        //given
        when(postService.getDTOById(1L)).thenReturn(post1DTO);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/blog/posts/1"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseJson = mvcResult.getResponse().getContentAsString();
        PostDTO resultPostDTO = objectMapper.readValue(responseJson, PostDTO.class);
        assertEquals(resultPostDTO, post1DTO);
    }

    @Test
    void editPost() throws Exception {
        //given
        PostDTO expected = PostDTO.builder()
                .id(1L)
                .body("newBody")
                .title("newTitle")
                .build();
        PostDTO updatedPostDTO = PostDTO.builder()
                .body("newBody")
                .title("newTitle")
                .build();
        when(postService.updatePost(1L, updatedPostDTO)).thenReturn(expected);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/blog/posts/edit/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPostDTO)))
                .andExpect(status().isOk())
                .andReturn();
        //then
        String responseJson = mvcResult.getResponse().getContentAsString();
        PostDTO resultPostDTO = objectMapper.readValue(responseJson, PostDTO.class);
        assertEquals(resultPostDTO, expected);
    }

    @Test
    void deletePostById() throws Exception {
        //given
        Long id = 1L;
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/blog/posts/delete/{id}", id))
                .andExpect(status().isOk());
        //then
        verify(postService, times(1)).deleteById(id);
    }

}