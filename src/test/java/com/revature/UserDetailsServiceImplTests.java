package com.revature;

import com.revature.entity.User;
import com.revature.repository.UserRepository;
import com.revature.service.UserService;
import com.revature.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserDetailsServiceImplTests {
    
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void testLoadUserByUsername() {
        User user = new User("testuser1", "P@ssw0rd");
        when(userService.findUserByUsername("testuser1")).thenReturn(new User(1, "a", "b"));
        UserDetails u = userDetailsService.loadUserByUsername("testuser1");
        assertNotNull(u);
    }
}
