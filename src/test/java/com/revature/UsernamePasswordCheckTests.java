package com.revature;

import com.revature.service.UsernamePasswordCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UsernamePasswordCheckTests {

    @Test
    public void testValid() {
        UsernamePasswordCheck c = new UsernamePasswordCheck("user", "Pa$$w0rd");
        assertTrue(c.isValidUsername());
        assertTrue(c.isValidPassword());
    }

    @Test
    public void testInvalid() {
        UsernamePasswordCheck c = new UsernamePasswordCheck("u", "p");
        assertFalse(c.isValidUsername());
        assertFalse(c.isValidPassword());
    }

    @Test
    public void testNull() {
        UsernamePasswordCheck c = new UsernamePasswordCheck(null, null);
        assertFalse(c.isValidUsername());
        assertFalse(c.isValidPassword());
    }
}
