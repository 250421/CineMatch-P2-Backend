package com.revature.controller;

import java.security.Principal;

import com.revature.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import com.revature.entity.Response;
import com.revature.entity.User;
import com.revature.service.UserService;
import com.revature.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    /**
     * Registers a new user if username does not already exist
     *
     * @param user User
     * @return Status code 400 if invalid username or password
     *         Status code 409 if username already exists
     *         Status code 201 and JSON of new user if successful
     */
    @PostMapping("/auth/register")
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody User user) {
        User newUser;
        try {
            newUser = userService.registerUser(user);
        } catch (InvalidUsernameException e) {
            return ResponseEntity.status(400)
                    .body(Response.stringResponse("Invalid username. Username must be at least 4 characters and contain only letters and numbers."));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(400)
                    .body(Response.stringResponse("Invalid password. " +
                            "Password must be at least 8 characters. " +
                            "A lower-case letter must occur at least once. " +
                            "An upper-case letter must occur at least once. " +
                            "A number must occur at least once. " +
                            "A special character must occur at least once."));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(409)
                    .body(Response.stringResponse("Username already exists."));
        }
        newUser.setPassword("");
        return ResponseEntity.status(201).body(newUser);
    }

    /**
     * Attempts to log in
     *
     * @param user User with username and password
     * @return Status code 401 if failed
     *         Status code 200 if successful
     */
    @PostMapping("/auth/login")
    public @ResponseBody ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        boolean auth = false;
        try {
            auth = authService.authenticate(user, request, response);
        }
        catch (BadCredentialsException e) {
            ResponseEntity.status(401).body(e.getMessage());
        }
        if (auth)
            return ResponseEntity.status(200).body(Response.stringResponse("Login successful!"));
        else return ResponseEntity.status(401).body(Response.stringResponse("Failed authentication with username: " + user.getUsername()));
    }

    /**
     * Fetches current session info
     * @param session Session
     * @param request Request
     * @return Status code 401 if no active session
     *         Status code 200 with session ID, username, role if active session
     */
    @GetMapping("/auth/session")
    public @ResponseBody ResponseEntity<?> session(HttpSession session, HttpServletRequest request, @CurrentSecurityContext SecurityContext context) {
        Authentication auth = context.getAuthentication();
        System.out.println(auth.getName());
        System.out.println(auth.isAuthenticated());
        System.out.println(auth.getPrincipal());
        Principal principal = request.getUserPrincipal();
        if (principal == null)
            return ResponseEntity.status(401).body(Response.stringResponse("No active session."));
        else {
            String username = principal.getName();
            String role = userService.findUserByUsername(username).getRole().name();
            return ResponseEntity.status(200).body(Response.sessionResponse(session.getId(), username, role));
        }
    }

    /**
     * Logs out and clears current session
     * @param authentication Authentication
     * @param request Request
     * @param response Response
     * @return Status code 200
     */
    @PostMapping("/auth/logout")
    public @ResponseBody ResponseEntity<?> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return ResponseEntity.status(200).body(Response.stringResponse("Logged out"));
    }
}
