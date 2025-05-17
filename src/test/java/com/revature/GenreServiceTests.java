package com.revature;

import com.revature.entity.Board;
import com.revature.entity.Genre;
import com.revature.entity.User;
import com.revature.exception.GenreAlreadyExistsException;
import com.revature.repository.GenreRepository;
import com.revature.repository.UserRepository;
import com.revature.service.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class GenreServiceTests {

    @Mock
    private GenreRepository genreRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GenreService genreService;

    @Test
    public void testAddNewGenre() {
        Genre genre = new Genre();
        genre.setName("Test");
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.empty());
        when(genreRepository.save(genre)).thenReturn(genre);
        assertEquals(genre, genreService.addGenre(genre));
    }

    @Test
    public void testAddExistingGenre() {
        Genre genre = new Genre();
        genre.setName("Test");
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.of(genre));
        assertThrows(GenreAlreadyExistsException.class, () -> {
            Genre result = genreService.addGenre(genre);
        });
    }

    @Test
    public void testAddGenres() {
        Genre genre = new Genre();
        genre.setName("Test");
        List<Genre> genres = new ArrayList<Genre>();
        genres.add(genre);
        when(genreRepository.findByName(genre.getName())).thenReturn(Optional.empty());
        when(genreRepository.findAll()).thenReturn(new ArrayList<Genre>());
        assertInstanceOf(List.class, genreService.addGenres(genres));
    }

    @Test
    public void testSetFavoriteGenres() {
        User user = new User();
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        when(genreRepository.findById(1)).thenReturn(Optional.of(new Genre()));
        when(genreRepository.findById(2)).thenReturn(Optional.of(new Genre()));
        when(genreRepository.findById(3)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(3, genreService.setFavoriteGenres(user, ids).size());
    }
}
