package com.revature;

import com.revature.entity.Board;
import com.revature.entity.Post;
import com.revature.entity.RatedPost;
import com.revature.entity.User;
import com.revature.repository.PostRepository;
import com.revature.repository.RatedPostRepository;
import com.revature.repository.UserRepository;
import com.revature.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PostServiceTests {

    @Mock
    private PostRepository postRepository;
    @Mock
    private RatedPostRepository ratedPostRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;


    @Test
    public void testAddPost() {
        Post textPost = new Post();

        when(postRepository.save(textPost)).thenReturn(textPost);

        assertEquals(textPost, postService.addPost(textPost, null));
    }

    @Test
    public void testFindPostById() {
        Post post = new Post();
        post.setId(1);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        Post savedPost = postService.findPostById(1);

        assertEquals(post, savedPost);
        assertEquals(savedPost.getId(), 1);
    }

    @Test
    public void testFindPostsByBoard() {
        Board board = new Board("Test");

        List<Post> postList = new ArrayList<>();

        Post testPost1 = new Post();
        testPost1.setBoard(board);
        postList.add(testPost1);

        Post testPost2 = new Post();
        testPost2.setBoard(board);
        postList.add(testPost2);

        when(postRepository.findByBoard(board)).thenReturn(postList);

        List<Post> savedPostList = postService.findPostsByBoard(board);

        assertEquals(postList.size(), savedPostList.size());

        for (int i = 0; i < postList.size(); i++) {
            assertEquals(postList.get(i), savedPostList.get(i));
            assertEquals(postList.get(i).getBoard(), savedPostList.get(i).getBoard());
        }
    }

    @Test
    public void testFindPostsByUser() {
        User user = new User();
        user.setUsername("TestUser");

        List<Post> postList = new ArrayList<>();

        Post testPost1 = new Post();
        testPost1.setUser(user);
        postList.add(testPost1);

        Post testPost2 = new Post();
        testPost2.setUser(user);
        postList.add(testPost2);

        when(postRepository.findByUser(user)).thenReturn(postList);

        List<Post> savedPostList = postService.findPostsByUser(user);

        assertEquals(postList.size(), savedPostList.size());

        for (int i = 0; i < postList.size(); i++) {
            assertEquals(postList.get(i), savedPostList.get(i));
            assertEquals(postList.get(i).getUser(), savedPostList.get(i).getUser());
        }
    }

    @Test
    public void testRatePostWhenRatingIsZero() {
        User user = new User();
        Post post = new Post();
        RatedPost ratedPost = new RatedPost();
        int rating = 0;

        when(ratedPostRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(ratedPost));
        doNothing().when(ratedPostRepository).delete(ratedPost);

        postService.ratePost(post, user, rating);

        verify(ratedPostRepository, times(1)).delete(ratedPost);
    }

    @Test
    public void testRatePostWhenRatingIsNotZero() {
        User user = new User();
        Post post = new Post();
        post.setRating(-2);
        RatedPost ratedPost = new RatedPost();
        ratedPost.setRating(-1);
        int rating = 1;

        when(ratedPostRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(ratedPost));
        when(ratedPostRepository.save(ratedPost)).thenReturn(ratedPost);
        when(postRepository.save(post)).thenReturn(post);

        Post savedPost = postService.ratePost(post, user, rating);

        assertEquals(1, ratedPost.getRating());
        assertEquals(0, savedPost.getRating());
    }

    @Test
    public void testAddFavoritePostById() {
        User user = new User();
        Post post = new Post();
        post.setId(1);
        user.setFavoritedPosts(new HashSet<>());

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(userRepository.save(user)).thenReturn(user);

        postService.addFavoritedPostById(1, user);

        assertEquals(1, user.getFavoritedPosts().size());
        assertTrue(user.getFavoritedPosts().contains(post));
    }

    @Test
    public void testAddFavoritePostByIdWhenNoPost() {
        User user = new User();
        user.setFavoritedPosts(new HashSet<>());

        when(postRepository.findById(1)).thenReturn(Optional.empty());

        postService.addFavoritedPostById(1, user);

        assertEquals(0, user.getFavoritedPosts().size());
    }

    @Test
    public void testRemoveFavoritePostById() {
        User user = new User();
        Post post = new Post();
        post.setId(1);
        Set<Post> favoritedPosts = new HashSet<>();
        favoritedPosts.add(post);
        user.setFavoritedPosts(favoritedPosts);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(userRepository.save(user)).thenReturn(user);

        boolean removed = postService.removeFavoritedPostById(1, user);

        assertEquals(0, user.getFavoritedPosts().size());
        assertTrue(removed);
    }

    @Test
    public void testGetFavoritePosts() {
        User user = new User();
        Post post = new Post();
        post.setId(1);
        Set<Post> favoritedPosts = new HashSet<>();
        favoritedPosts.add(post);
        user.setFavoritedPosts(favoritedPosts);

        List<Post> savedFavoritePosts = postService.getFavoritedPosts(user);

        assertEquals(List.copyOf(favoritedPosts).get(0), savedFavoritePosts.get(0));
    }


}
