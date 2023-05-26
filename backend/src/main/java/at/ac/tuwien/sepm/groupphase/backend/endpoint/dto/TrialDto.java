package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;

public record TrialDto(
    Long id,
    @NotBlank(message = "Title must not be blank")
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Researcher researcher,
    @NotBlank(message = "Study type must not be Blank")

    String studyType,
    String briefSummary,
    @NotBlank(message = "Detailed summary must not be blank")
    String detailedSummary,
    String sponsor,
    String collaborator,
    TrialStatus status,
    @NotBlank(message = "Location must not be blank")
    String location,
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
