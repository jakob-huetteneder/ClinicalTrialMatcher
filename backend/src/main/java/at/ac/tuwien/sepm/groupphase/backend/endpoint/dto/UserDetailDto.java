package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
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

    Role role,

    @NotNull(message = "Status must not be null")
    ApplicationUser.Status status
) {
}