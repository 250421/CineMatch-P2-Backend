package com.revature;

import com.revature.entity.*;
import com.revature.exception.BoardAlreadyExistsException;
import com.revature.repository.BoardRepository;
import com.revature.repository.CommentRepository;
import com.revature.repository.RatedCommentRepository;
import com.revature.repository.UserRepository;
import com.revature.service.BoardService;
import com.revature.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private RatedCommentRepository ratedCommentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void testAddComment() {
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);
        assertEquals(comment, commentService.addComment(comment));
    }

    @Test
    public void testFindCommentById() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(new Comment()));
        assertInstanceOf(Comment.class, commentService.findCommentById(1));
    }

    @Test
    public void testFindCommentsByPost() {
        Post post = new Post();
        when(commentRepository.findByPost(post)).thenReturn(new ArrayList<Comment>());
        assertInstanceOf(List.class, commentService.findCommentsByPost(post));
    }

    @Test
    public void testFindCommentsByUser() {
        User user = new User();
        when(commentRepository.findByUser(user)).thenReturn(new ArrayList<Comment>());
        assertInstanceOf(List.class, commentService.findCommentsByUser(user));
    }

    @Test
    public void testRateComment1() {
        Comment comment = new Comment();
        comment.setRating(-1);
        User user = new User();
        int rating = 1;
        RatedComment rc = new RatedComment();
        rc.setRating(-1);
        when(ratedCommentRepository.findByUserAndComment(user, comment)).thenReturn(Optional.of(rc));
        when(ratedCommentRepository.save(rc)).thenReturn(rc);
        when(commentRepository.save(comment)).thenReturn(comment);
        assertEquals(1, commentService.rateComment(comment, user, rating).getRating());
    }

    @Test
    public void testRateComment2() {
        Comment comment = new Comment();
        comment.setRating(-1);
        User user = new User();
        int rating = 0;
        RatedComment rc = new RatedComment();
        rc.setRating(-1);
        when(ratedCommentRepository.findByUserAndComment(user, comment)).thenReturn(Optional.of(rc));
        when(commentRepository.save(comment)).thenReturn(comment);
        assertEquals(0, commentService.rateComment(comment, user, rating).getRating());
    }

    @Test
    public void testRateComment3() {
        Comment comment = new Comment();
        comment.setRating(0);
        User user = new User();
        int rating = 1;
        when(ratedCommentRepository.findByUserAndComment(user, comment)).thenReturn(Optional.empty());
        when(ratedCommentRepository.save(new RatedComment(user, comment, rating))).thenReturn(new RatedComment(user, comment, rating));
        when(commentRepository.save(comment)).thenReturn(comment);
        assertEquals(1, commentService.rateComment(comment, user, rating).getRating());
    }

    @Test
    public void testAddFavoritedCommentById1() {
        int id = 1;
        User user = new User();
        user.setFavoritedComments(new HashSet<Comment>());
        when(commentRepository.findById(1)).thenReturn(Optional.of(new Comment()));
        when(userRepository.save(user)).thenReturn(user);
        commentService.addFavoritedCommentById(1, user);
        assertEquals(1, user.getFavoritedComments().size());
    }

    @Test
    public void testAddFavoritedCommentById2() {
        int id = 1;
        User user = new User();
        user.setFavoritedComments(new HashSet<Comment>());
        when(commentRepository.findById(1)).thenReturn(Optional.empty());
        commentService.addFavoritedCommentById(1, user);
        assertEquals(0, user.getFavoritedComments().size());
    }

    @Test
    public void testRemoveFavoritedCommentById() {
        int id = 1;
        User user = new User();
        user.setFavoritedComments(new HashSet<Comment>());
        when(commentRepository.findById(1)).thenReturn(Optional.of(new Comment()));
        when(userRepository.save(user)).thenReturn(user);
        assertFalse(commentService.removeFavoritedCommentById(1, user));
    }

    @Test
    public void testGetFavoritedComments() {
        User user = new User();
        user.setFavoritedComments(new HashSet<Comment>());
        assertInstanceOf(List.class, commentService.getFavoritedComments(user));
    }
}
