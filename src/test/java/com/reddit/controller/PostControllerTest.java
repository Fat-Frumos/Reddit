package com.reddit.controller;

import com.reddit.model.dto.PostResponse;
import com.reddit.service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @WithMockUser(username = "testUser", roles = "USER")
    @ArgumentsSource(PostResponseArgumentsProvider.class)
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts/")
    void shouldCreatePost(List<PostResponse> postResponses) throws Exception {
        when(postService.getAllPosts()).thenReturn(ResponseEntity.ok().body(postResponses));
        mockMvc.perform(get("/api/post")
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", Matchers.is(2)))
               .andExpect(jsonPath("$[0].id", Matchers.is(1)))
               .andExpect(jsonPath("$[0].postName", Matchers.is("Post Name")))
               .andExpect(jsonPath("$[0].url", Matchers.is("https://url.site")))
               .andExpect(jsonPath("$[1].url", Matchers.is("https://url2.site2")))
               .andExpect(jsonPath("$[1].postName", Matchers.is("Post Name 2")))
               .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }

    @ParameterizedTest
    @DisplayName("Should get post by ID")
    @WithMockUser(username = "testUser", roles = "USER")
    @ArgumentsSource(PostResponseArgumentsProvider.class)
    void testGetPost(List<PostResponse> postResponses) throws Exception {
        PostResponse postResponse = postResponses.get(0);

        when(postService.getPostById(postResponse.getId()))
                .thenReturn(new ResponseEntity<>(postResponse, HttpStatus.OK));

        mockMvc.perform(get("/api/post/{id}", postResponse.getId())
               .contentType(APPLICATION_JSON)
               .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(postResponse.getId()))
               .andExpect(jsonPath("$.postName").value(postResponse.getPostName()));
    }

    @ParameterizedTest
    @DisplayName("Should get all post responses")
    @WithMockUser(username = "testUser", roles = "USER")
    @ArgumentsSource(PostResponseArgumentsProvider.class)
    void testGetAllPostResponse(List<PostResponse> postResponses) throws Exception {
        when(postService.getAllPosts())
                .thenReturn(new ResponseEntity<>(postResponses, HttpStatus.OK));

        mockMvc.perform(get("/api/post")
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(postResponses.size())))
               .andExpect(jsonPath("$[0].id").value(postResponses.get(0).getId()))
               .andExpect(jsonPath("$[0].postName").value(postResponses.get(0).getPostName()));
    }

    @ParameterizedTest
    @DisplayName("Should get posts by subreddit")
    @WithMockUser(username = "testUser", roles = "USER")
    @ArgumentsSource(PostResponseArgumentsProvider.class)
    void testGetBySubReddit(List<PostResponse> postResponses) throws Exception {
        PostResponse postResponse = postResponses.get(0);

        when(postService.getPostsBySubReddit(postResponse.getId()))
                .thenReturn(new ResponseEntity<>(postResponses, HttpStatus.OK));

        mockMvc.perform(get("/api/post/reddit/{id}", postResponse.getId())
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(postResponse.getId()))
               .andExpect(jsonPath("$[0].postName").value(postResponse.getPostName()));
    }

    @ParameterizedTest
    @DisplayName("Should get posts by username")
    @WithMockUser(username = "testUser", roles = "USER")
    @ArgumentsSource(PostResponseArgumentsProvider.class)
    void testGetByUsername(List<PostResponse> postResponses) throws Exception {
        PostResponse postResponse = postResponses.get(0);

        when(postService.getPostsByUsername(postResponse.getUserName()))
                .thenReturn(new ResponseEntity<>(postResponses, HttpStatus.OK));

        mockMvc.perform(get("/api/post/user/{name}", postResponse.getUserName())
                                .contentType(APPLICATION_JSON)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(postResponse.getId()))
               .andExpect(jsonPath("$[0].postName").value(postResponse.getPostName()));
    }
}
