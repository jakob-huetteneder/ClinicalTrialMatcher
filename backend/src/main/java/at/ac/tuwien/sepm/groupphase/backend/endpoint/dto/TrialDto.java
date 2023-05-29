package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public record TrialDto(
    Long id,
    @NotBlank(message = "Title must not be blank")
    String title,
    LocalDate startDate,
    LocalDate endDate,
    @NotNull(message = "Researcher must not be null")
    Researcher researcher,
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
    TrialStatus status,
    @NotBlank(message = "Location must not be blank")
    String location,
    @NotNull(message = "Gender must not be null")
    Gender crGender,
    @Min(value = 0, message = "Minimum age must be greater than 0")
    int crMinAge,
    @Min(value = 0, message = "Maximum age must be greater than 0")
    int crMaxAge,
    String crFreeText
) {
    @Override
    public Long id() {
        return id;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public LocalDate startDate() {
        return startDate;
    }

    @Override
    public LocalDate endDate() {
        return endDate;
    }

    @Override
    public Researcher researcher() {
        return researcher;
    }

    @Override
    public String studyType() {
        return studyType;
    }

    @Override
    public String briefSummary() {
        return briefSummary;
    }

    @Override
    public String detailedSummary() {
        return detailedSummary;
    }

    @Override
    public String sponsor() {
        return sponsor;
    }

    @Override
    public String collaborator() {
        return collaborator;
    }

    @Override
    public TrialStatus status() {
        return status;
    }

    @Override
    public String location() {
        return location;
    }

    @Override
    public Gender crGender() {
        return crGender;
    }

    @Override
    public int crMinAge() {
        return crMinAge;
    }

    @Override
    public int crMaxAge() {
        return crMaxAge;
    }

    @Override
    public String crFreeText() {
        return crFreeText;
    }
}
