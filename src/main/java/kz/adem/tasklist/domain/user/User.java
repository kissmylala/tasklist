package kz.adem.tasklist.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.adem.tasklist.domain.task.Task;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;

}
