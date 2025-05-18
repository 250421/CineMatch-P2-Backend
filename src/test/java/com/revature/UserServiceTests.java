package com.revature;

import com.revature.entity.User;
import com.revature.exception.InvalidPasswordException;
import com.revature.exception.InvalidUsernameException;
import com.revature.exception.UsernameAlreadyExistsException;
import com.revature.repository.UserRepository;
import com.revature.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    public void testRegisterUser() {
        User user = new User("testuser1", "P@ssw0rd");
        User newUser = new User(1, user.getUsername(), user.getPassword());
        when(userRepository.findByUsername("testuser1")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(newUser);
        when(bCryptPasswordEncoder.encode("P@ssw0rd")).thenReturn("P@ssw0rd");
        User result = userService.registerUser(user);
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(result.getPassword(), user.getPassword());
    }

    @Test
    public void testRegisterDuplicateUser() {
        User user = new User("testuser1", "P@ssw0rd");
        when(userRepository.findByUsername("testuser1")).thenReturn(Optional.of(user));
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            User result = userService.registerUser(user);
        });
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User("testuser1", "P@ssw0rd");
        when(userRepository.findByUsername("testuser1")).thenReturn(Optional.of(user));
        assertEquals(user, userService.findUserByUsername("testuser1"));
    }

    @Test
    public void testInvalidUsername() {
        User user = new User("a", "P@ssw0rd");
        when(userRepository.findByUsername("a")).thenReturn(Optional.empty());
        assertThrows(InvalidUsernameException.class, () -> {
            User result = userService.registerUser(user);
        });
    }

    @Test
    public void testInvalidPassword() {
        User user = new User("testuser1", "P");
        when(userRepository.findByUsername("testuser1")).thenReturn(Optional.empty());
        assertThrows(InvalidPasswordException.class, () -> {
            User result = userService.registerUser(user);
        });
    }
}
