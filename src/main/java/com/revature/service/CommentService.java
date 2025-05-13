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
        Comment newComment = commentRepository.save(comment);
        newComment.getUser().setPassword("");
        return newComment;
    }

    public Comment findCommentById(int id) {
        Comment comment;
        comment = commentRepository.findById(id)
                .orElse(null);
        if (comment != null)
            comment.getUser().setPassword("");
        return comment;
    }

    public List<Comment> findCommentsByPost(Post post) {
        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment c : comments) {
            c.getUser().setPassword("");
        }
        return comments;
    }

    public List<Comment> findCommentsByUser(User user) {
        List<Comment> comments = commentRepository.findByUser(user);
        for (Comment c : comments) {
            c.getUser().setPassword("");
        }
        return comments;
    }

    //TODO
    public void rateComment(int rating) {

    }
}
