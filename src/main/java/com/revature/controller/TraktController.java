package com.revature.controller;

import com.revature.entity.Genre;
import com.revature.service.BoardService;
import com.revature.service.GenreService;
import com.revature.service.TraktService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TraktController {

    @Autowired
    private GenreService genreService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private TraktService traktService;

    //Calls Trakt API to get a list of genres, persist genres to database, then create a board for each genre
    //Ignores duplicate genres and boards that already exist
    @PostMapping("/api/trakt")
    public @ResponseBody ResponseEntity<?> addGenresAndBoardsFromTrakt() {
        List<Genre> traktGenres = traktService.getGenres();
        List<Genre> newGenres = genreService.addGenres(traktGenres);
        return ResponseEntity.status(201).body(boardService.addBoardsFromGenres(newGenres));
    }
}
