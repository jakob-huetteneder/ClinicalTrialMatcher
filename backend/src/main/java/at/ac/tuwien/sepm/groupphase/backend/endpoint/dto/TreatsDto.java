package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;

public record TreatsDto(
    PatientDto patient,
    UserDetailDto doctor,
    Treats.Status status
) {
}
