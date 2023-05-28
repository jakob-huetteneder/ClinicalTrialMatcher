package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public record SearchDto(
    String name,
    Integer limit
) {
}