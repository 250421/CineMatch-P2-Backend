package com.revature.service;

import com.revature.entity.Board;
import com.revature.entity.Post;
import com.revature.entity.User;
import com.revature.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post addPost(Post post) {
        Post newPost = postRepository.save(post);
        newPost.getUser().setPassword("");
        return newPost;
    }

    public Post findPostById(int id) {
        Post post;
        post = postRepository.findById(id)
                .orElse(null);
        if (post != null)
            post.getUser().setPassword("");
        return post;
    }

    public List<Post> findPostsByBoard(Board board) {
        List<Post> posts = postRepository.findByBoard(board);
        for (Post p : posts) {
            p.getUser().setPassword("");
        }
        return posts;
    }

    public List<Post> findPostsByUser(User user) {
        List<Post> posts = postRepository.findByUser(user);
        for (Post p : posts) {
            p.getUser().setPassword("");
        }
        return posts;
    }

    //TODO
    public void ratePost(int rating) {

    }
}
