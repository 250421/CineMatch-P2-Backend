package com.revature.service;

import com.revature.entity.Board;
import com.revature.entity.Genre;
import com.revature.exception.BoardAlreadyExistsException;
import com.revature.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public Board addBoard(Board board) {
        Board newBoard = boardRepository.findByName(board.getName())
                .orElse(null);
        if (newBoard != null)
            throw new BoardAlreadyExistsException("");
        return boardRepository.save(board);
    }

    public List<Board> addBoardsFromGenres(List<Genre> genres) {
        for (Genre genre : genres) {
            Board board = findBoardByName(genre.getName());
            if (board == null) {
                Board newBoard = new Board(genre.getName());
                boardRepository.save(newBoard);
            }
        }
        return findAllBoards();
    }

    public Board findBoardById(int id) {
        Board board;
        board = boardRepository.findById(id)
                .orElse(null);
        return board;
    }

    public Board findBoardByName(String name) {
        Board board;
        board = boardRepository.findByName(name)
                .orElse(null);
        return board;
    }

    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }
}
