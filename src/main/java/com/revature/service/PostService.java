package com.revature.service;

import com.revature.entity.Board;
import com.revature.entity.Post;
import com.revature.entity.RatedPost;
import com.revature.entity.User;
import com.revature.repository.PostRepository;
import com.revature.repository.RatedPostRepository;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RatedPostRepository ratedPostRepository;
    @Autowired
    private UserRepository userRepository;

    public Post addPost(Post post, MultipartFile imageFile) {

        return postRepository.save(post);
    }

    public Post findPostById(int id) {
        Post post;
        post = postRepository.findById(id)
                .orElse(null);
        return post;
    }

    public List<Post> findPostsByBoard(Board board) {
        return postRepository.findByBoard(board);
    }

    public List<Post> findPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    public Post ratePost(Post post, User user, int rating) {
        RatedPost currentRP = ratedPostRepository.findByUserAndPost(user, post).orElse(null);
        int currentRating = 0;
        if (currentRP != null) {
            currentRating = currentRP.getRating();
            if (rating == 0)
                ratedPostRepository.delete(currentRP);
            else {
                currentRP.setRating(rating);
                ratedPostRepository.save(currentRP);
            }
        }
        else if (rating != 0)
            ratedPostRepository.save(new RatedPost(user, post, rating));
        int diff = rating - currentRating;
        post.setRating(post.getRating() + diff);
        return postRepository.save(post);
    }

    public Post addFavoritedPostById(int id, User user) {
        Post post = findPostById(id);
        if (post != null) {
            user.getFavoritedPosts().add(post);
            userRepository.save(user);
            return post;
        }
        else return null;
    }

    public boolean removeFavoritedPostById(int id, User user) {
        Post post = findPostById(id);
        boolean removed = user.getFavoritedPosts().remove(post);
        userRepository.save(user);
        return removed;
    }

    public List<Post> getFavoritedPosts(User user) {
        return List.copyOf(user.getFavoritedPosts());
    }
}
