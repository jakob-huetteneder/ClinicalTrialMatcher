package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record FilterDto(
    Gender gender,
    TrialStatus recruiting,
    LocalDate startDate,
    LocalDate endDate,
    @Min(value = 0, message = "Minimum age must be greater than 0")
    int minAge,
    @Min(value = 0, message = "Maximum age must be greater than 0")
    int maxAge
) {
}
