package com.revature.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
}
