package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public record DiseaseDto(
    Long id,
    String name,
    String synonyms
) {
}