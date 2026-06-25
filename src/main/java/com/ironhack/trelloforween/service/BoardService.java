package com.ironhack.trelloforween.service;

import com.ironhack.trelloforween.entity.Board;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.ironhack.trelloforween.exception.BoardNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name, User owner) {
        Board board = Board.builder()
                .name(name)
                .owner(owner)
                .build();
        return boardRepository.save(board);
    }

    public List<Board> getMyBoards(User user) {
        return boardRepository.findByOwnerId(user.getId());
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("Board not found"));
    }
}
