package com.revature;

import com.revature.entity.Board;
import com.revature.entity.Genre;
import com.revature.exception.BoardAlreadyExistsException;
import com.revature.repository.BoardRepository;
import com.revature.service.BoardService;
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
public class BoardServiceTests {

	@Mock
	private BoardRepository boardRepository;
	@InjectMocks
	private BoardService boardService;

	@Test
	public void testAddNewBoard() {
		Board board = new Board("Test");
		when(boardRepository.findByName("Test")).thenReturn(Optional.empty());
		when(boardRepository.save(board)).thenReturn(board);
		assertEquals(board, boardService.addBoard(board));
	}

	@Test
	public void testAddExistingBoard() {
		Board board = new Board("Test");
		when(boardRepository.findByName("Test")).thenReturn(Optional.of(new Board("Test")));
		assertThrows(BoardAlreadyExistsException.class, () -> {
			Board result = boardService.addBoard(board);
		});
	}

	@Test
	public void testAddBoardsFromGenres() {
		List<Genre> genres = new ArrayList<>();
		Genre genre = new Genre();
		genre.setName("Test");
		genres.add(genre);
		List<Board> boards = new ArrayList<>();
		Board board = new Board("Test");
		boards.add(board);
		when(boardRepository.findByName("Test")).thenReturn(Optional.empty());
		when(boardRepository.findAll()).thenReturn(boards);
		assertEquals(boards, boardService.addBoardsFromGenres(genres));
	}

	@Test
	public void testFindBoardById() {
		when(boardRepository.findById(1)).thenReturn(Optional.of(new Board("Test")));
		assertInstanceOf(Board.class, boardService.findBoardById(1));
	}
}
