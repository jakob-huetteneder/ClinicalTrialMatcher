package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientRequestDto(
    Long id,
    @NotNull(message = "First name must not be null")
    String firstName,
    @NotNull(message = "Last name must not be null")
    String lastName,
    @NotNull(message = "Birthdate must not be null")
    LocalDate birthdate,
    @NotNull(message = "Gender must not be null")
    Gender gender,
    Treats treats
) {

}
