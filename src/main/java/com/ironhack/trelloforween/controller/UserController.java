package com.ironhack.trelloforween.controller;

import com.ironhack.trelloforween.dto.UserResponseDto;
import com.ironhack.trelloforween.dto.UserUpdateRequestDto;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing user accounts")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user", description = "Retrieves the currently authenticated user.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal User userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(mapToResponse(userDetails));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users.")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves details of a specific user by their ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(mapToResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user", description = "Updates details like name and profile picture for an existing user.")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequestDto request) {
        try {
            User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setName(request.getName());
            user.setProfilePicture(request.getProfilePicture());
            return ResponseEntity.ok(mapToResponse(userService.updateUser(id, user)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete user", description = "Deletes a user account by their ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    private UserResponseDto mapToResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}
