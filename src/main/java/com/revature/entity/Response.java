package com.revature.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class Response {

    public static Map<String, Object> stringResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return response;
    }

    public static Map<String, Object> sessionResponse(String sessionId, String username, String role) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", sessionId);
        response.put("username", username);
        response.put("role", role);
        return response;
    }

    public static Map<String, Object> postResponse(Post post) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", post.getId());
        response.put("username", post.getUser().getUsername());
        response.put("title", post.getTitle());
        response.put("image", post.getImage());
        response.put("text", post.getText());
        response.put("has_spoiler", post.getHasSpoiler());
        response.put("rating", post.getRating());
        response.put("deleted", post.getDeleted());
        return response;
    }

    public static List<Map<String, Object>> postListResponse(List<Post> list) {
        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
        for (Post p : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", p.getId());
            m.put("username", p.getUser().getUsername());
            m.put("title", p.getTitle());
            m.put("image", p.getImage());
            m.put("text", p.getText());
            m.put("has_spoiler", p.getHasSpoiler());
            m.put("rating", p.getRating());
            m.put("deleted", p.getDeleted());
            response.add(m);
        }
        return response;
    }

    public static Map<String, Object> commentResponse(Comment comment) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", comment.getId());
        response.put("username", comment.getUser().getUsername());
        response.put("text", comment.getText());
        response.put("rating", comment.getRating());
        response.put("deleted", comment.getDeleted());
        return response;
    }

    public static List<Map<String, Object>> commentListResponse(List<Comment> list) {
        List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
        for (Comment c : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("username", c.getUser().getUsername());
            m.put("text", c.getText());
            m.put("rating", c.getRating());
            m.put("deleted", c.getDeleted());
            response.add(m);
        }
        return response;
    }
}
