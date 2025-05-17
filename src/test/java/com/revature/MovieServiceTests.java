package com.revature;

import com.revature.entity.Movie;
import com.revature.entity.User;
import com.revature.exception.MovieAlreadyExistsException;
import com.revature.repository.MovieRepository;
import com.revature.repository.UserRepository;
import com.revature.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MovieServiceTests {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MovieService movieService;

    @Test
    public void testAddNewMovie() {
        Movie movie = new Movie();
        movie.setTitle("Test");
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.empty());
        when(movieRepository.save(movie)).thenReturn(movie);
        assertEquals(movie, movieService.addMovie(movie));
    }

    @Test
    public void testAddExistingMovie() {
        Movie movie = new Movie();
        movie.setTitle("Test");
        when(movieRepository.findByTitle(movie.getTitle())).thenReturn(Optional.of(movie));
        assertThrows(MovieAlreadyExistsException.class, () -> {
            Movie result = movieService.addMovie(movie);
        });
    }

    @Test
    public void testAddMovies() {
        List<Movie> movies = new ArrayList<Movie>();
        when(movieRepository.saveAll(movies)).thenReturn(movies);
        assertInstanceOf(List.class, movieService.addMovies(movies));
    }

    @Test
    public void testFindAllMovies() {
        when(movieRepository.findAll()).thenReturn(new ArrayList<Movie>());
        assertInstanceOf(List.class, movieService.findAllMovies());
    }

    @Test
    public void testSetFavoriteMoviesById() {
        User user = new User();
        user.setFavoritedMovies(new HashSet<Movie>());
        List<Integer> movies = new ArrayList<Integer>();
        movies.add(1);
        movies.add(2);
        when(movieRepository.findById(1)).thenReturn(Optional.of(new Movie()));
        when(movieRepository.findById(2)).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(1, movieService.setFavoriteMoviesById(user, movies).size());
    }

    @Test
    public void testGetFavoriteMovies() {
        User user = new User();
        Set<Movie> favorites = new HashSet<Movie>();
        user.setFavoritedMovies(favorites);
        assertEquals(favorites, movieService.getFavoriteMovies(user));
    }
}
