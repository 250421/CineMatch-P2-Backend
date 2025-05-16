package com.revature.service;

import com.revature.entity.*;
import com.revature.repository.CommentRepository;
import com.revature.repository.RatedCommentRepository;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RatedCommentRepository ratedCommentRepository;
    @Autowired
    private UserRepository userRepository;

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

    public Comment rateComment(Comment comment, User user, int rating) {
        RatedComment currentRC = ratedCommentRepository.findByUserAndComment(user, comment).orElse(null);
        int currentRating = 0;
        if (currentRC != null) {
            currentRating = currentRC.getRating();
            if (rating == 0)
                ratedCommentRepository.delete(currentRC);
            else {
                currentRC.setRating(rating);
                ratedCommentRepository.save(currentRC);
            }
        }
        else if (rating != 0)
            ratedCommentRepository.save(new RatedComment(user, comment, rating));
        int diff = rating - currentRating;
        comment.setRating(comment.getRating() + diff);
        return commentRepository.save(comment);
    }

    public Comment addFavoritedCommentById(int id, User user) {
        Comment comment = findCommentById(id);
        if (comment != null) {
            user.getFavoritedComments().add(comment);
            userRepository.save(user);
            return comment;
        }
        else return null;
    }

    public boolean removeFavoritedCommentById(int id, User user) {
        Comment comment = findCommentById(id);
        boolean removed = user.getFavoritedComments().remove(comment);
        userRepository.save(user);
        return removed;
    }

    public List<Comment> getFavoritedComments(User user) {
        return List.copyOf(user.getFavoritedComments());
    }
}
