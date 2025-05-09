package com.revature.service;

import com.revature.entity.Movie;
import com.revature.exception.MovieAlreadyExistsException;
import com.revature.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

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

    public Movie getMovieById(int id) {
        Movie movie;
        movie = movieRepository.findById(id)
                .orElse(null);
        return movie;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
