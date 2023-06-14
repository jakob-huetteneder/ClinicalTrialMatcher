package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

public record DiseaseDto(
    Long id,
    @NotNull(message = "Name must not be null")
    String name,
    String link
) {
}