package com.revature.service;

import com.revature.entity.Genre;
import com.revature.entity.User;
import com.revature.exception.GenreAlreadyExistsException;
import com.revature.repository.GenreRepository;
import com.revature.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private UserRepository userRepository;

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

    public List<String> setFavoriteGenres(User user, List<String> genres) {
        user.setGenre_1(genres.get(0));
        user.setGenre_2(genres.get(1));
        user.setGenre_3(genres.get(2));
        userRepository.save(user);
        return getFavoriteGenres(user);
    }

    public List<String> getFavoriteGenres(User user) {
        ArrayList<String> fav = new ArrayList<>(3);
        fav.add(user.getGenre_1());
        fav.add(user.getGenre_2());
        fav.add(user.getGenre_3());
        return fav;
    }
}
