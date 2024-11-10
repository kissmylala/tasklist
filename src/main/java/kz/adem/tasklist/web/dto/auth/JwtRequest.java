package kz.adem.tasklist.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO for representing JWT authentication request")
public class JwtRequest {

    @Schema(name = "username", description = "Username for authentication", example = "john_doe",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Username must be not null.")
    private String username;

    @Schema(name = "password", description = "Password for authentication", example = "secure_password",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Password must be not null.")
    private String password;
}
