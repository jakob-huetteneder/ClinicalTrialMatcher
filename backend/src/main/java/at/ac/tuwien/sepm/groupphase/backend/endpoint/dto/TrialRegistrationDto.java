package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;

public record TrialRegistrationDto(
    Long id,
    PatientDto patient,
    TrialDto trial,
    Registration.Status status
) {
}
