package com.revature.repository;

import com.revature.entity.Post;
import com.revature.entity.RatedPost;
import com.revature.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatedPostRepository extends JpaRepository<RatedPost, Integer> {

    Optional<RatedPost> findById(int id);

    Optional<RatedPost> findByUserAndPost(User user, Post post);

    List<RatedPost> findByUser(User user);

    List<RatedPost> findByPost(Post post);
}
