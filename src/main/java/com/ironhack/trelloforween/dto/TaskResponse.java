package com.ironhack.trelloforween.dto;

import com.ironhack.trelloforween.entity.TaskStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Long boardId;
    private UserSummaryDto assignedUser;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
}
