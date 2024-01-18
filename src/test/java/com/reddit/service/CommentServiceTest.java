package com.reddit.service;

import com.reddit.exception.SpringRedditException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest {

    private CommentService commentService;

    @BeforeEach
    public void setup() {
        commentService = new JpaCommentService(null, null, null, null, null);
    }

    @Test
    @DisplayName("Test Should Pass When Comment do not Contains Swear Words")
    void shouldNotContainSwearWordsInsideComment() {
        assertThat(commentService.containsSwearWords("This is a comment")).isFalse();
    }

//    @Test
    @DisplayName("Should Throw Exception when Exception Contains Swear Words")
    void shouldFailWhenCommentContainsSwearWords() {
        assertThatThrownBy(() -> commentService.containsSwearWords("This is a shitty comment"))
                .isInstanceOf(SpringRedditException.class).hasMessage("Comments contains unacceptable language");
    }
}
