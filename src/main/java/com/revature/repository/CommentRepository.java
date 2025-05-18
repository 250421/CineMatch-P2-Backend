package com.revature.repository;

import com.revature.entity.Comment;
import com.revature.entity.Post;
import com.revature.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findById(int id);

    List<Comment> findByUser(User user);

    List<Comment> findByPost(Post post);

    List<Comment> findByDeleted(int deleted);
}
