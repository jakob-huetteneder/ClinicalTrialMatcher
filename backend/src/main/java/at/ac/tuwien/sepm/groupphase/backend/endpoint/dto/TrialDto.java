package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.time.LocalDate;

public record TrialDto(
    Long id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Researcher researcher,
    String studyType,
    String briefSummary,
    String detailedSummary,
    String sponsor,
    String collaborator,
    String status,
    String location,
    Gender crGender,
    int crMinAge,
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
    public String status() {
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
