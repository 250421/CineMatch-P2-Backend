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

    //TODO
    public void ratePost(int rating) {

    }
}
