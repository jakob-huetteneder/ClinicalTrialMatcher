package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public record DiseaseSearchDto(
    String name,
    Integer limit
) {
}