package com.revature.service;

import com.revature.entity.Post;
import com.revature.entity.Comment;
import com.revature.entity.User;
import com.revature.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(int id) {
        Comment comment;
        comment = commentRepository.findById(id)
                .orElse(null);
        return comment;
    }

    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findByPost(post);
    }

    public List<Comment> findCommentsByUser(User user) {
        return commentRepository.findByUser(user);
    }

    //TODO
    public void rateComment(int rating) {

    }
}
