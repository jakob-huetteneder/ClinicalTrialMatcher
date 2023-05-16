package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;

public record UserDetailDto(
    Long id,

    String firstName,

    String lastName,

    String email,

    String password,

    String role,

    Status status
) {
}