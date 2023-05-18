package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.time.LocalDate;
import java.util.Set;

public record PatientDto(
    Long id,
    Long applicationUserId,
    String firstName,
    String lastName,
    String email,
    String admissionNote,
    LocalDate birthdate,
    Gender gender,
    Set<Long> doctorIds,
    Set<Long> diagnoseIds
) {
}
