package kz.adem.tasklist.web.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kz.adem.tasklist.validation.OnCreate;
import kz.adem.tasklist.validation.OnUpdate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "DTO for representing a user")
public class UserDTO {

    @Schema(name = "id", description = "Unique identifier of the user", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Id must be not null.", groups = OnUpdate.class)
    private Long id;

    @Schema(name = "name", description = "Full name of the user", example = "John Doe",
            maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Name must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.", groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @Schema(name = "username", description = "Username for user login", example = "john_doe",
            maxLength = 255, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Username must be not null", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols.", groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @Schema(name = "password", description = "Password for user authentication", example = "secure_password",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnUpdate.class, OnCreate.class})
    private String password;

    @Schema(name = "passwordConfirmation", description = "Password confirmation for user registration", example = "secure_password",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.WRITE_ONLY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = {OnCreate.class})
    private String passwordConfirmation;
}
