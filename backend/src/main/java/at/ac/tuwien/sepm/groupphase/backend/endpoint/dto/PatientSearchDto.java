package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

public record PatientSearchDto(
    Long id,
    UserDetailDto applicationUser,
    @NotNull(message = "First name must not be null")
    String firstName,
    @NotNull(message = "Last name must not be null")
    String lastName,
    String email,
    String admissionNote,
    @NotNull(message = "Birthdate must not be null")
    LocalDate birthdate,
    @NotNull(message = "Gender must not be null")
    Gender gender,
    Set<DiagnoseDto> diagnoses,
    Set<ExaminationDto> examinations,
    Float score
) {
}
