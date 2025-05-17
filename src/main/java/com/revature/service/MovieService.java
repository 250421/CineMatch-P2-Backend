package com.revature.service;

import com.revature.entity.Movie;
import com.revature.entity.User;
import com.revature.exception.MovieAlreadyExistsException;
import com.revature.repository.MovieRepository;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    public Movie addMovie(Movie movie) throws MovieAlreadyExistsException {
        Movie newMovie = movieRepository.findByTitle(movie.getTitle())
                .orElse(null);
        if (newMovie != null)
            throw new MovieAlreadyExistsException("");
        return movieRepository.save(movie);
    }

    public List<Movie> addMovies(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    public Movie findMovieById(int id) {
        Movie movie;
        movie = movieRepository.findById(id)
                .orElse(null);
        return movie;
    }

    public List<Movie> findAllMovies() {
        return movieRepository.findAll();
    }

    public Set<Movie> setFavoriteMoviesById(User user, Iterable<Integer> movies) {
        user.getFavoritedMovies().clear();
        for (Integer id : movies) {
            Movie movie = findMovieById(id);
            if (movie != null)
                user.getFavoritedMovies().add(movie);
        }
        userRepository.save(user);
        return user.getFavoritedMovies();
    }

    public Set<Movie> getFavoriteMovies(User user) {
        return user.getFavoritedMovies();
    }
}
