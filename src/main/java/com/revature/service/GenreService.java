package com.revature.service;

import com.revature.entity.Genre;
import com.revature.exception.GenreAlreadyExistsException;
import com.revature.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre addGenre(Genre genre) {
        Genre newGenre = genreRepository.findByName(genre.getName())
                .orElse(null);
        if (newGenre != null)
            throw new GenreAlreadyExistsException("");
        return genreRepository.save(genre);
    }

    public List<Genre> addGenres(List<Genre> genres) {
        return genreRepository.saveAll(genres);
    }

    public Genre findGenreById(int id) {
        Genre genre;
        genre = genreRepository.findById(id)
                .orElse(null);
        return genre;
    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }
}
