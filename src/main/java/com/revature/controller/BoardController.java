package com.revature.controller;

import com.revature.entity.*;
import com.revature.exception.BoardAlreadyExistsException;
import com.revature.service.BoardService;
import com.revature.service.CommentService;
import com.revature.service.PostService;
import com.revature.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BoardController {

    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    //Add a new board
    @PostMapping("/api/board")
    public @ResponseBody ResponseEntity<?> addBoard(@RequestBody Board board) {
        Board newBoard;
        try {
            newBoard = boardService.addBoard(board);
        } catch (BoardAlreadyExistsException e) {
            return ResponseEntity.status(409)
                    .body(Response.stringResponse("Board already exists."));
        }
        return ResponseEntity.status(201).body(newBoard);
    }

    //Get one board
    @GetMapping("/api/board/{id}")
    public @ResponseBody ResponseEntity<?> getBoardById(@PathVariable int id) {
        Board board = boardService.findBoardById(id);
        if (board != null)
            return ResponseEntity.status(200).body(board);
        else return ResponseEntity.status(404).body(Response.stringResponse("Board not found."));
    }

    //Get all boards
    @GetMapping("/api/board")
    public @ResponseBody ResponseEntity<?> getAllBoards() {
        return ResponseEntity.status(200).body(boardService.findAllBoards());
    }

    //Add a new post
    @PostMapping("/api/board/{id}/post")
    public @ResponseBody ResponseEntity<?> addPost(@PathVariable int id, @RequestBody Post post, HttpServletRequest request) {
        Board board = boardService.findBoardById(id);
        if (board == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Board not found."));
        Principal principal = request.getUserPrincipal();
        if (principal == null)
            return ResponseEntity.status(401).body(Response.stringResponse("No active session."));
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        post.setUser(user);
        post.setBoard(board);
        post.setRating(0);
        post.setDeleted(0);
        return ResponseEntity.status(201).body(postService.addPost(post));
    }

    //Edit a post TODO

    //Rate a post TODO

    //Delete a post
    @DeleteMapping("/api/post/{id}")
    public @ResponseBody ResponseEntity<?> deletePostById(@PathVariable int id, HttpServletRequest request) {
        Post post = postService.findPostById(id);
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (post != null) {
            if (user.equals(post.getUser()) || user.getRole().equals(Role.ADMIN)) {
                post.setDeleted(1);
                postService.addPost(post);
                return ResponseEntity.status(200).body(Response.stringResponse("Post deleted."));
            }
            else return ResponseEntity.status(401).body(Response.stringResponse("Unauthorized."));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
    }

    //Get one post
    @GetMapping("/api/post/{id}")
    public @ResponseBody ResponseEntity<?> getPostById(@PathVariable int id) {
        Post post = postService.findPostById(id);
        if (post != null)
            return ResponseEntity.status(200).body(post);
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
    }

    //Get all posts on a board
    @GetMapping("/api/board/{id}/post")
    public @ResponseBody ResponseEntity<?> getAllPostsByBoard(@PathVariable int id) {
        Board board = boardService.findBoardById(id);
        if (board == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Board not found."));
        return ResponseEntity.status(200).body(postService.findPostsByBoard(board));
    }

    //Add a new comment
    @PostMapping("/api/post/{id}/comment")
    public @ResponseBody ResponseEntity<?> addComment(@PathVariable int id, @RequestBody Comment comment, HttpServletRequest request) {
        Post post = postService.findPostById(id);
        if (post == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
        Principal principal = request.getUserPrincipal();
        if (principal == null)
            return ResponseEntity.status(401).body(Response.stringResponse("No active session."));
        String username = principal.getName();
        User user = userService.findUserByUsername(username);
        comment.setUser(user);
        comment.setPost(post);
        comment.setRating(0);
        comment.setDeleted(0);
        return ResponseEntity.status(201).body(commentService.addComment(comment));
    }

    //Edit a comment TODO

    //Rate a comment TODO

    //Delete a comment
    @DeleteMapping("/api/comment/{id}")
    public @ResponseBody ResponseEntity<?> deleteCommentById(@PathVariable int id, HttpServletRequest request) {
        Comment comment = commentService.findCommentById(id);
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (comment != null) {
            if (user.equals(comment.getUser()) || user.getRole().equals(Role.ADMIN)) {
                comment.setDeleted(1);
                commentService.addComment(comment);
                return ResponseEntity.status(200).body(Response.stringResponse("Comment deleted."));
            }
            else return ResponseEntity.status(401).body(Response.stringResponse("Unauthorized."));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
    }

    //Get one comment
    @GetMapping("/api/comment/{id}")
    public @ResponseBody ResponseEntity<?> getCommentById(@PathVariable int id) {
        Comment comment = commentService.findCommentById(id);
        if (comment != null)
            return ResponseEntity.status(200).body(comment);
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
    }

    //Get all comments on a post
    @GetMapping("/api/post/{id}/comment")
    public @ResponseBody ResponseEntity<?> getAllCommentsByPost(@PathVariable int id) {
        Post post = postService.findPostById(id);
        if (post == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
        return ResponseEntity.status(200).body(commentService.findCommentsByPost(post));
    }
}
