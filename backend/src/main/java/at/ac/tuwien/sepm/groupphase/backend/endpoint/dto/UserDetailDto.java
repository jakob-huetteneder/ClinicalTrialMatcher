package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDetailDto(
    Long id,

    @NotEmpty(message = "First name must not be empty")
    @Size(min = 1, max = 255, message = "First name must be between 1 and 255 characters")
    String firstName,

    @NotEmpty(message = "Last name must not be empty")
    @Size(min = 1, max = 255, message = "Last name must be between 1 and 255 characters")
    String lastName,

    @NotEmpty(message = "Email must not be empty")
    @Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters")
    @Email(message = "Email must be valid")
    String email,

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password,

    Role role,

    @NotNull(message = "Status must not be null")
    Status status
) {
}