package com.reddit.service;

import com.reddit.model.dto.PostRequest;
import com.reddit.model.dto.PostResponse;
import com.reddit.model.entity.Post;
import com.reddit.model.entity.Role;
import com.reddit.model.entity.SubReddit;
import com.reddit.model.entity.User;
import com.reddit.repository.PostRepository;
import com.reddit.repository.SubRedditRepository;
import com.reddit.service.auth.AuthService;
import com.reddit.service.facade.FacadeRepositoryService;
import com.reddit.service.mapper.PostMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private SubRedditRepository subredditRepository;
    @Mock
    private FacadeRepositoryService repository;
    @Mock
    private AuthService authService;
    @Mock
    private PostMapper postMapper;
    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    private PostService postService;

    @BeforeEach
    public void setup() {
        postService = new JpaPostService(postMapper, authService, repository);
    }

    @Test
    @DisplayName("Should Retrieve Post by Id")
    void shouldFindPostById() {
        Post post = new Post(123L, "First Post", "https://url.site", "Test", 0, null, Instant.now(), null);
        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "https://url.site", "Test", "Test User", "Test Subredit", 0, 0, "1 Hour Ago", false, false);
        when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        when(postMapper.toDto(Mockito.any(Post.class))).thenReturn(
                expectedPostResponse);
        PostResponse actualPostResponse = postService.getPostById(123L).getBody();
        assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        assertThat(actualPostResponse.getPostName()).isEqualTo(
                expectedPostResponse.getPostName());
    }

    @Test
    @DisplayName("Should Save Posts")
    void shouldSavePosts() {
        User currentUser = new User(123L, "test user", "secret password", "user@email.com", Instant.now(), "read", true, Role.USER);
        SubReddit subreddit = new SubReddit(123L, "First Subreddit", "Subreddit Description", emptyList(), Instant.now(), currentUser);
        Post post = new Post(123L, "First Post", "https://url.site", "Test", 0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(1L, "https://url.site", "First Subreddit", "First Post", "100", 0, "Test", "Test", 0);

        when(subredditRepository.findByName("First Subreddit")).thenReturn(Optional.of(subreddit));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(postMapper.toEntity(postRequest, subreddit, currentUser)).thenReturn(post);
        postService.save(postRequest);
        verify(postRepository, times(1)).save(postArgumentCaptor.capture());
        assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        assertThat(postArgumentCaptor.getValue().getPostName()).isEqualTo("First Post");
    }
}
