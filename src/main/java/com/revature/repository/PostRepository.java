package com.revature.repository;

import com.revature.entity.Board;
import com.revature.entity.Post;
import com.revature.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById(int id);

    Optional<Post> findByTitle(String title);

    List<Post> findByUser(User user);

    List<Post> findByBoard(Board board);

    List<Post> findByBoardOrderByCreatedDesc(Board board);

    List<Post> findByDeleted(int deleted);
}
