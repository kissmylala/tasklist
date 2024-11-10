package kz.adem.tasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.adem.tasklist.service.TaskService;
import kz.adem.tasklist.web.dto.mappers.TaskMapper;
import kz.adem.tasklist.web.dto.task.TaskDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Tasks", description = "Operations related to managing tasks")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    @PostAuthorize("canAccessTask(#id) and returnObject.id==2")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskMapper.toDTO(taskService.getById(id));
    }

    @Operation(summary = "Delete task by ID", description = "Delete a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteTaskById(@PathVariable Long id) {
        taskService.delete(id);
    }

    @Operation(summary = "Update task", description = "Update an existing task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping
    @PreAuthorize("canAccessTask(#taskDTO.id)")
    public void updateTaskById(@RequestBody TaskDTO taskDTO) {
        taskService.update(taskMapper.toEntity(taskDTO));
    }
}
