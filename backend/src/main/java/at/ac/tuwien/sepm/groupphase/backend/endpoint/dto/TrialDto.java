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
    @NotBlank(message = "Study type must not be Blank")
    String studyType,
    @NotBlank(message = "Brief summary must not be blank")
    String briefSummary,
    @NotBlank(message = "Detailed summary must not be blank")
    String detailedSummary,
    @NotBlank(message = "Sponsor must not be blank")
    String sponsor,
    @NotBlank(message = "Collaborator must not be blank")
    String collaborator,
    @NotNull(message = "Status must not be null")
    Trial.Status status,
    @NotBlank(message = "Location must not be blank")
    String location,
    @NotNull(message = "Gender must not be null")
    Gender crGender,
    @Min(value = 0, message = "Minimum age must be greater than 0")
    int crMinAge,
    @Min(value = 0, message = "Maximum age must be greater than 0")
    int crMaxAge,
    List<String> inclusionCriteria,
    List<String> exclusionCriteria
) {
}
