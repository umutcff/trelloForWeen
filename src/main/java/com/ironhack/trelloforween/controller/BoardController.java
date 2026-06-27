package com.ironhack.trelloforween.controller;

import com.ironhack.trelloforween.dto.BoardResponse;
import com.ironhack.trelloforween.dto.UserSummaryDto;
import com.ironhack.trelloforween.entity.Board;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.service.BoardService;
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Boards", description = "Endpoints for managing project boards")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "Create a new board", description = "Creates a new project board with the authenticated user as the owner.")
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody com.ironhack.trelloforween.dto.BoardCreateRequest request, @AuthenticationPrincipal User user) {
        Board board = boardService.createBoard(request.getName(), user);
        return ResponseEntity.ok(mapToResponse(board));
    }

    @Operation(summary = "Get user's boards", description = "Retrieves all boards owned by the authenticated user.")
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getMyBoards(@AuthenticationPrincipal User user) {
        List<BoardResponse> boards = boardService.getMyBoards(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(boards);
    }

    private BoardResponse mapToResponse(Board board) {
        UserSummaryDto ownerDto = UserSummaryDto.builder()
                .id(board.getOwner().getId())
                .name(board.getOwner().getName())
                .email(board.getOwner().getEmail())
                .build();
                
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .owner(ownerDto)
                .build();
    }
}
