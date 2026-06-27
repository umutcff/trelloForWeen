package com.ironhack.trelloforween.service;

import com.ironhack.trelloforween.dto.TaskCreateRequest;
import com.ironhack.trelloforween.dto.TaskUpdateRequest;
import com.ironhack.trelloforween.entity.Board;
import com.ironhack.trelloforween.entity.Task;
import com.ironhack.trelloforween.entity.TaskStatus;
import com.ironhack.trelloforween.entity.User;
import com.ironhack.trelloforween.exception.BoardNotFoundException;
import com.ironhack.trelloforween.exception.UserNotFoundException;
import com.ironhack.trelloforween.repository.BoardRepository;
import com.ironhack.trelloforween.repository.TaskRepository;
import com.ironhack.trelloforween.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public Task createTask(TaskCreateRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException("Board not found"));
                
        User assignedUser = null;
        if (request.getAssignedUserId() != null) {
            assignedUser = userRepository.findById(request.getAssignedUserId())
                    .orElseThrow(() -> new UserNotFoundException("Assigned user not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.TO_DO)
                .board(board)
                .assignedUser(assignedUser)
                .dueDate(request.getDueDate())
                .build();
                
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskUpdateRequest request) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            
            if (request.getStatus() != null) {
                task.setStatus(request.getStatus());
            }
            
            if (request.getAssignedUserId() != null) {
                User assignedUser = userRepository.findById(request.getAssignedUserId())
                        .orElseThrow(() -> new UserNotFoundException("Assigned user not found"));
                task.setAssignedUser(assignedUser);
            }
            
            task.setDueDate(request.getDueDate());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
