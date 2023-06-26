package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TrialDto(
    Long id,
    @NotBlank(message = "Title must not be blank")
    String title,
    LocalDate startDate,
    LocalDate endDate,
    UserDetailDto researcher,
    String studyType,

    String briefSummary,

    String detailedSummary,

    String sponsor,

    String collaborator,
    @NotNull(message = "Status must not be null")
    Trial.Status status,

    String location,
    @NotNull(message = "Gender must not be null")
    Gender crGender,
    @Min(value = 0, message = "Minimum age must be greater than 0")
    int crMinAge,
    @Min(value = 0, message = "Maximum age must be greater than 0")
    int crMaxAge,
    List<String> inclusionCriteria,
    List<String> exclusionCriteria,
    List<DiseaseDto> diseases
) {
}
