package com.ironhack.trelloforween.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskCreateRequest {
    private String title;
    private String description;
    private Long boardId;
    private Long assignedUserId;
    private LocalDateTime dueDate;
}
