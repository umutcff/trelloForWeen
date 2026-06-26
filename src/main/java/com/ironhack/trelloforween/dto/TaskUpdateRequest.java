package com.ironhack.trelloforween.dto;

import com.ironhack.trelloforween.entity.TaskStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private Long assignedUserId;
    private LocalDateTime dueDate;
}
