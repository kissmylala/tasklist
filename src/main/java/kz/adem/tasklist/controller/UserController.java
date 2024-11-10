package kz.adem.tasklist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.adem.tasklist.domain.task.Task;
import kz.adem.tasklist.domain.user.User;
import kz.adem.tasklist.service.TaskService;
import kz.adem.tasklist.service.UserService;
import kz.adem.tasklist.validation.OnCreate;
import kz.adem.tasklist.validation.OnUpdate;
import kz.adem.tasklist.web.dto.mappers.TaskMapper;
import kz.adem.tasklist.web.dto.mappers.UserMapper;
import kz.adem.tasklist.web.dto.task.TaskDTO;
import kz.adem.tasklist.web.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "Users", description = "Operations related to managing users")
public class UserController {
    private final UserService userService;

    private final TaskService taskService;

    private final UserMapper userMapper;

    private final TaskMapper taskMapper;

    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDTO getUserById(@PathVariable Long id) {
        return userMapper.toDTO(userService.getById(id));
    }

    @Operation(summary = "Update user by ID", description = "Update an existing user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userDTO.id)")
    public UserDTO updateUserById(@RequestBody @Validated(OnUpdate.class) UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        return userMapper.toDTO(userService.update(user));
    }

    @Operation(summary = "Delete user by ID", description = "Delete a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteUserById(@PathVariable Long id) {
        userService.delete(id);
    }

    @Operation(summary = "Get tasks by user ID", description = "Retrieve all tasks associated with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        return taskMapper.toDTO(taskService.getAllByUserId(id));
    }

    @Operation(summary = "Create task for user", description = "Create a new task associated with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDTO createTask(@PathVariable Long id, @RequestBody @Validated(OnCreate.class) TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        return taskMapper.toDTO(taskService.create(task, id));
    }
}
