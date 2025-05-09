package com.revature.controller;

import com.revature.exception.GenreAlreadyExistsException;
import com.revature.exception.MovieAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revature.entity.Response;
import com.revature.entity.Genre;
import com.revature.entity.Movie;
import com.revature.service.GenreService;
import com.revature.service.MovieService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MovieController {

    @Autowired
    private GenreService genreService;
    @Autowired
    private MovieService movieService;

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

    @GetMapping("/api/genre/{id}")
    public @ResponseBody ResponseEntity<?> getGenreById(@PathVariable int id) {
        Genre genre = genreService.getGenreById(id);
        if (genre != null)
            return ResponseEntity.status(200).body(genre);
        else return ResponseEntity.status(404).body(Response.stringResponse("Genre not found."));
    }

    @GetMapping("/api/genre")
    public @ResponseBody ResponseEntity<?> getAllGenres() {
        return ResponseEntity.status(200).body(genreService.getAllGenres());
    }

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

    @GetMapping("/api/movie/{id}")
    public @ResponseBody ResponseEntity<?> getMovieById(@PathVariable int id) {
        Movie movie = movieService.getMovieById(id);
        if (movie != null)
            return ResponseEntity.status(200).body(movie);
        else return ResponseEntity.status(404).body(Response.stringResponse("Movie not found."));
    }

    @GetMapping("/api/movie")
    public @ResponseBody ResponseEntity<?> getAllMovies() {
        return ResponseEntity.status(200).body(movieService.getAllMovies());
    }
}
