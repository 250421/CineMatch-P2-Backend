package com.revature;

import com.revature.service.TraktService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TraktServiceTests {

    @Autowired
    private TraktService traktService;

    @Test
    public void testGetGenres() {
        assertEquals(27, traktService.getGenres().size());
    }
}
