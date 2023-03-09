package ua.shamray.myblogspringbootv1.integration.controller.with_security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ua.shamray.myblogspringbootv1.controller.PostController;
import ua.shamray.myblogspringbootv1.dto.AccountViewer;
import ua.shamray.myblogspringbootv1.dto.PostDTO;
import ua.shamray.myblogspringbootv1.entity.Account;
import ua.shamray.myblogspringbootv1.entity.Role;
import ua.shamray.myblogspringbootv1.entity.RoleType;
import ua.shamray.myblogspringbootv1.security.MyUser;
import ua.shamray.myblogspringbootv1.service.AccountService;
import ua.shamray.myblogspringbootv1.service.AuthenticationService;
import ua.shamray.myblogspringbootv1.service.PostService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc
class PostControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private AccountService accountService;
    private PostDTO post1DTO;
    private Account account;
    private MyUser myUser;

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .id(1L)
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
        post1DTO = PostDTO.builder()
                .accountViewer(accountViewer)
                .body("postBody")
                .title("postTitle")
                .build();
        myUser = MyUser.myBuilder()
                .username(account.getEmail())
                .password(account.getPassword())
                .authorities(Set.of(new SimpleGrantedAuthority("ROLE_USER")))
                .userID(account.getId())
                .build();
    }

    @Test
    void getAllUserPosts_shouldAccessDeniedForUnauthorized() throws Exception {
        mockMvc.perform(get("/blog/posts/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllUserPosts_shouldReturnOkForAuthorized() throws Exception {
        mockMvc.perform(get("/blog/posts/my")
                        .with(user(myUser)))
                .andExpect(status().isOk());
    }

    @Test
    void createPost_shouldForbiddenForUnauthorized()throws Exception {
        mockMvc.perform(post("/blog/posts/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post1DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createPost_shouldReturnOkForAuthorizedAndValidPostDTO() throws Exception {
        //given
        when(authenticationService.getAuthenticatedUserByEmail(anyString()))
                .thenReturn(account);
        when(postService.saveNewPost(post1DTO, account))
                .thenReturn(post1DTO);
        //when
        MvcResult mvcResult = mockMvc.perform(post("/blog/posts/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post1DTO)))
                .andExpect(status().isCreated())
                .andReturn();
        //then
        String resultString = mvcResult.getResponse().getContentAsString();
        PostDTO resultPostDTO = objectMapper.readValue(resultString, PostDTO.class);
        assertEquals(resultPostDTO, post1DTO);
    }

    @Test
    void editPost_shouldAccessDeniedForUnauthorized() throws Exception {
        mockMvc.perform(post("/blog/posts/edit/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1")
                        .content(objectMapper.writeValueAsString(post1DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void editPost_shouldReturnOkForAuthorizedAndValidPostDTO() throws Exception {
        //given
        PostDTO updateDTO = PostDTO.builder()
                .title("newTitle")
                .body("newBody")
                .build();
        PostDTO expectedDTO = PostDTO.builder()
                .id(1L)
                .title("newTitle")
                .body("newBody")
                .accountViewer(post1DTO.getAccountViewer())
                .build();
        when(postService.updatePost(1L, updateDTO)).thenReturn(expectedDTO);
        //when
        MvcResult mvcResult = mockMvc.perform(put("/blog/posts/edit/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1")
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //then
        String resultString = mvcResult.getResponse().getContentAsString();
        PostDTO resultPostDTO = objectMapper.readValue(resultString, PostDTO.class);
        assertEquals(resultPostDTO, expectedDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editPost_shouldReturnOkForAdminRole() throws Exception {
        editPost_shouldReturnOkForAuthorizedAndValidPostDTO();
    }

    @Test
    void deletePostById_shouldAccessDeniedForUnauthorized() throws Exception {
        mockMvc.perform(post("/blog/posts/delete/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1")
                        .content(objectMapper.writeValueAsString(post1DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void deletePostById_shouldReturnOkForAuthorizedUser() throws Exception {
        //given
        Long id = 1L;
        //when
        mockMvc.perform(delete("/blog/posts/delete/{id}", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        //then
       verify(postService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePostById_shouldReturnOkForAdmin() throws Exception {
        deletePostById_shouldReturnOkForAuthorizedUser();
    }

}