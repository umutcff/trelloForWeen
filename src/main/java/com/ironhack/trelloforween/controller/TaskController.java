package com.ironhack.trelloforween.controller;

import com.ironhack.trelloforween.dto.TaskCreateRequest;
import com.ironhack.trelloforween.dto.TaskResponse;
import com.ironhack.trelloforween.dto.TaskUpdateRequest;
import com.ironhack.trelloforween.dto.UserSummaryDto;
import com.ironhack.trelloforween.entity.Task;
import com.ironhack.trelloforween.entity.TaskStatus;
import com.ironhack.trelloforween.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks.")
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Get task by ID", description = "Retrieves details of a specific task by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(mapToResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get tasks by status", description = "Retrieves all tasks that have a specific status.")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Create a new task", description = "Creates a new task. Assigining to a user is optional.")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskCreateRequest request) {
        return ResponseEntity.ok(mapToResponse(taskService.createTask(request)));
    }

    @Operation(summary = "Update an existing task", description = "Updates details, status or assignment of an existing task.")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        try {
            return ResponseEntity.ok(mapToResponse(taskService.updateTask(id, request)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    
    private TaskResponse mapToResponse(Task task) {
        UserSummaryDto assignedUserDto = null;
        if (task.getAssignedUser() != null) {
            assignedUserDto = UserSummaryDto.builder()
                    .id(task.getAssignedUser().getId())
                    .name(task.getAssignedUser().getName())
                    .email(task.getAssignedUser().getEmail())
                    .build();
        }
        
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .boardId(task.getBoard() != null ? task.getBoard().getId() : null)
                .assignedUser(assignedUserDto)
                .createdAt(task.getCreatedAt())
                .dueDate(task.getDueDate())
                .build();
    }
}
