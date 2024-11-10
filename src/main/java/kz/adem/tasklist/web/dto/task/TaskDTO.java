package kz.adem.tasklist.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kz.adem.tasklist.domain.task.Status;
import kz.adem.tasklist.validation.OnCreate;
import kz.adem.tasklist.validation.OnUpdate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Schema(description = "DTO for representing a task")
public class TaskDTO {

    @Schema(name = "id", description = "Unique identifier of the task", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Id must be not null.", groups = OnUpdate.class)
    private Long id;

    @Schema(name = "title", description = "Title of the task", example = "Complete the report",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Title must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String title;

    @Schema(name = "description", description = "Brief description of the task", example = "Prepare the Q4 report for the meeting",
            maxLength = 255)
    @Length(max = 255, message = "Description length must be smaller than 255 symbols.", groups = {OnUpdate.class, OnCreate.class})
    private String description;

    @Schema(name = "status", description = "Current status of the task", example = "IN_PROGRESS")
    private Status status;

    @Schema(name = "expirationDate", description = "Expiration date and time for the task", example = "2023-12-31T23:59:59",
            type = "string", format = "date-time")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;
}
