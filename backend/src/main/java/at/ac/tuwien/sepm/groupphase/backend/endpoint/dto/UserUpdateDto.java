package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    Long id,

    @Size(min = 1, max = 255, message = "First name must be between 1 and 255 characters")
    String firstName,

    @Size(min = 1, max = 255, message = "Last name must be between 1 and 255 characters")
    String lastName,

    @Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters")
    @Email(message = "Email must be valid")
    String email,

    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password,

    Role role,

    Status status,

    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
        String oldPassword
) {

    public UserUpdateDto withId(Long id) {
        return new UserUpdateDto(id, firstName, lastName, email, password, role, status, oldPassword);
    }


}
