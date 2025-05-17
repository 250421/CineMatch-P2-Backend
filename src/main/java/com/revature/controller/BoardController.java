package com.revature.controller;

import com.revature.entity.*;
import com.revature.exception.BoardAlreadyExistsException;
import com.revature.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
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
    public @ResponseBody ResponseEntity<?> addPost(@PathVariable int id, @RequestPart Post post, @RequestPart MultipartFile imageFile, HttpServletRequest request) {
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
        return ResponseEntity.status(201).body(Response.postResponse(postService.addPost(post)));
    }

    //Edit a post
    @PatchMapping("/api/post")
    public @ResponseBody ResponseEntity<?> editPost(@RequestBody Post post, HttpServletRequest request) {
        Post oldPost = postService.findPostById(post.getId());
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (oldPost != null) {
            if (user.equals(oldPost.getUser())) {
                oldPost.setTitle(post.getTitle());
                oldPost.setText(post.getText());
                oldPost.setImage(post.getImage());
                oldPost.setHasSpoiler(post.getHasSpoiler());
                return ResponseEntity.status(201).body(Response.postResponse(postService.addPost(oldPost)));
            }
            else return ResponseEntity.status(401).body(Response.stringResponse("Unauthorized."));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
    }

    //Rate a post
    @PatchMapping("/api/post/{id}")
    public @ResponseBody ResponseEntity<?> ratePostById(@PathVariable int id, @RequestBody Integer rating, HttpServletRequest request) {
        if (rating > 1 || rating < -1)
            return ResponseEntity.status(400).body(Response.stringResponse("Rating must be 1, 0, or -1."));
        Post post = postService.findPostById(id);
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (post != null) {
            return ResponseEntity.status(201).body(Response.postResponse(postService.ratePost(post, user, rating)));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
    }

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
            return ResponseEntity.status(200).body(Response.postResponse(post));
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
    }

    //Get all posts on a board
    @GetMapping("/api/board/{id}/post")
    public @ResponseBody ResponseEntity<?> getAllPostsByBoard(@PathVariable int id) {
        Board board = boardService.findBoardById(id);
        if (board == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Board not found."));
        return ResponseEntity.status(200).body(Response.postListResponse(postService.findPostsByBoard(board)));
    }

    //Add a favorited post
    @PostMapping("/api/post/favorite")
    public @ResponseBody ResponseEntity<?> addFavoritedPostById(@RequestBody Integer id, HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        Post post = postService.addFavoritedPostById(id, user);
        if (post == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
        return ResponseEntity.status(201).body(Response.postResponse(post));
    }

    //Remove a favorited post
    @DeleteMapping("/api/post/favorite")
    public @ResponseBody ResponseEntity<?> removeFavoritedPostById(@RequestBody Integer id, HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        boolean removed = postService.removeFavoritedPostById(id, user);
        if (removed)
            return ResponseEntity.status(200).body(Response.stringResponse("Post removed from favorited."));
        else return ResponseEntity.status(404).body(Response.stringResponse("Post not in favorited."));
    }

    //Get current user's favorited posts
    @GetMapping("/api/post/favorite")
    public @ResponseBody ResponseEntity<?> getFavoritedPosts(HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        return ResponseEntity.status(200).body(Response.postListResponse(postService.getFavoritedPosts(user)));
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
        return ResponseEntity.status(201).body(Response.commentResponse(commentService.addComment(comment)));
    }

    //Edit a comment
    @PatchMapping("/api/comment")
    public @ResponseBody ResponseEntity<?> editComment(@RequestBody Comment comment, HttpServletRequest request) {
        Comment oldComment = commentService.findCommentById(comment.getId());
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (oldComment != null) {
            if (user.equals(oldComment.getUser())) {
                oldComment.setText(comment.getText());
                return ResponseEntity.status(201).body(Response.commentResponse(commentService.addComment(oldComment)));
            }
            else return ResponseEntity.status(401).body(Response.stringResponse("Unauthorized."));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
    }

    //Rate a comment
    @PatchMapping("/api/comment/{id}")
    public @ResponseBody ResponseEntity<?> rateCommentById(@PathVariable int id, @RequestBody Integer rating, HttpServletRequest request) {
        if (rating > 1 || rating < -1)
            return ResponseEntity.status(400).body(Response.stringResponse("Rating must be 1, 0, or -1."));
        Comment comment = commentService.findCommentById(id);
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        if (comment != null) {
            return ResponseEntity.status(201).body(Response.commentResponse(commentService.rateComment(comment, user, rating)));
        }
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
    }

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
            return ResponseEntity.status(200).body(Response.commentResponse(comment));
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
    }

    //Get all comments on a post
    @GetMapping("/api/post/{id}/comment")
    public @ResponseBody ResponseEntity<?> getAllCommentsByPost(@PathVariable int id) {
        Post post = postService.findPostById(id);
        if (post == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Post not found."));
        return ResponseEntity.status(200).body(Response.commentListResponse(commentService.findCommentsByPost(post)));
    }

    //Add a favorited comment
    @PostMapping("/api/comment/favorite")
    public @ResponseBody ResponseEntity<?> addFavoritedCommentById(@RequestBody Integer id, HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        Comment comment = commentService.addFavoritedCommentById(id, user);
        if (comment == null)
            return ResponseEntity.status(404).body(Response.stringResponse("Comment not found."));
        return ResponseEntity.status(201).body(Response.commentResponse(comment));
    }

    //Remove a favorited comment
    @DeleteMapping("/api/comment/favorite")
    public @ResponseBody ResponseEntity<?> removeFavoritedCommentById(@RequestBody Integer id, HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        boolean removed = commentService.removeFavoritedCommentById(id, user);
        if (removed)
            return ResponseEntity.status(200).body(Response.stringResponse("Comment removed from favorited."));
        else return ResponseEntity.status(404).body(Response.stringResponse("Comment not in favorited."));
    }

    //Get current user's favorited comments
    @GetMapping("/api/comment/favorite")
    public @ResponseBody ResponseEntity<?> getFavoritedComments(HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        return ResponseEntity.status(200).body(Response.commentListResponse(commentService.getFavoritedComments(user)));
    }
}
