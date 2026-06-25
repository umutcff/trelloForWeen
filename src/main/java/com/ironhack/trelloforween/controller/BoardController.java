package com.ironhack.trelloforween.controller;

import com.ironhack.trelloforween.entity.Board;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Board board, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(boardService.createBoard(board.getName(), user));
    }

    @GetMapping
    public ResponseEntity<List<Board>> getMyBoards(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(boardService.getMyBoards(user));
    }
}
