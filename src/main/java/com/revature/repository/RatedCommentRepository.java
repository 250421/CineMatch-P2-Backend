package com.revature.repository;

import com.revature.entity.Comment;
import com.revature.entity.RatedComment;
import com.revature.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatedCommentRepository extends JpaRepository<RatedComment, Integer> {

    Optional<RatedComment> findById(int id);

    Optional<RatedComment> findByUserAndComment(User user, Comment comment);

    List<RatedComment> findByUser(User user);

    List<RatedComment> findByComment(Comment comment);
}
