package com.revature.service;

import com.revature.entity.User;
import com.revature.exception.InvalidPasswordException;
import com.revature.exception.InvalidUsernameException;
import com.revature.exception.UsernameAlreadyExistsException;
import com.revature.exception.UsernameNotFoundException;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Registers a new user if username does not already exist
     * @param user User
     * @return New registered user or null if username already exists
     */
    public User registerUser(User user)
            throws InvalidUsernameException, InvalidPasswordException, UsernameAlreadyExistsException {
        User newUser = userRepository.findByUsername(user.getUsername())
                .orElse(null);
        if (newUser != null)
            throw new UsernameAlreadyExistsException("");
        UsernamePasswordCheck UPCheck = new UsernamePasswordCheck(user.getUsername(), user.getPassword());
        if (!UPCheck.isValidUsername())
            throw new InvalidUsernameException("");
        if (!UPCheck.isValidPassword())
            throw new InvalidPasswordException("");
        else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));
    }
}
