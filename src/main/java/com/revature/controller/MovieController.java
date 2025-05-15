package com.revature.controller;

import com.revature.entity.User;
import com.revature.exception.GenreAlreadyExistsException;
import com.revature.exception.MovieAlreadyExistsException;
import com.revature.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revature.entity.Response;
import com.revature.entity.Genre;
import com.revature.entity.Movie;
import com.revature.service.GenreService;
import com.revature.service.MovieService;

import java.util.List;

@RestController
public class MovieController {

    @Autowired
    private GenreService genreService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;

    //Add a new genre
    @PostMapping("/api/genre")
    public @ResponseBody ResponseEntity<?> addGenre(@RequestBody Genre genre) {
        Genre newGenre;
        try {
            newGenre = genreService.addGenre(genre);
        } catch (GenreAlreadyExistsException e) {
            return ResponseEntity.status(409)
                    .body(Response.stringResponse("Genre already exists."));
        }
        return ResponseEntity.status(201).body(newGenre);
    }

    //Get one genre
    @GetMapping("/api/genre/{id}")
    public @ResponseBody ResponseEntity<?> getGenreById(@PathVariable int id) {
        Genre genre = genreService.findGenreById(id);
        if (genre != null)
            return ResponseEntity.status(200).body(genre);
        else return ResponseEntity.status(404).body(Response.stringResponse("Genre not found."));
    }

    //Get all genres
    @GetMapping("/api/genre")
    public @ResponseBody ResponseEntity<?> getAllGenres() {
        return ResponseEntity.status(200).body(genreService.findAllGenres());
    }

    //Set current user's favorite genres
    @PostMapping("/api/genre/favorite")
    public @ResponseBody ResponseEntity<?> setFavoriteGenres(@RequestBody List<String> genres, HttpServletRequest request) {
        if (genres.size() != 3)
            return ResponseEntity.status(400).body(Response.stringResponse("Genre list length must be 3."));
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        return ResponseEntity.status(201).body(genreService.setFavoriteGenres(user, genres));
    }

    //Get current user's favorite genres
    @GetMapping("/api/genre/favorite")
    public @ResponseBody ResponseEntity<?> getFavoriteGenres(HttpServletRequest request) {
        User user = userService.findUserByUsername(request.getUserPrincipal().getName());
        return ResponseEntity.status(200).body(genreService.getFavoriteGenres(user));
    }

    //Add a new movie
    @PostMapping("/api/movie")
    public @ResponseBody ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        Movie newMovie;
        try {
            newMovie = movieService.addMovie(movie);
        } catch (MovieAlreadyExistsException e) {
            return ResponseEntity.status(409)
                    .body(Response.stringResponse("Movie already exists."));
        }
        return ResponseEntity.status(201).body(newMovie);
    }

    //Get one movie
    @GetMapping("/api/movie/{id}")
    public @ResponseBody ResponseEntity<?> getMovieById(@PathVariable int id) {
        Movie movie = movieService.findMovieById(id);
        if (movie != null)
            return ResponseEntity.status(200).body(movie);
        else return ResponseEntity.status(404).body(Response.stringResponse("Movie not found."));
    }

    //Get all movies
    @GetMapping("/api/movie")
    public @ResponseBody ResponseEntity<?> getAllMovies() {
        return ResponseEntity.status(200).body(movieService.findAllMovies());
    }

    //Set current user's favorite movies TODO

    //Get current user's favorite movies TODO
}
