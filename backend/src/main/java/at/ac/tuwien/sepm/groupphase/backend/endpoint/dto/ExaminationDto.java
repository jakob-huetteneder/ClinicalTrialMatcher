package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;

public record ExaminationDto(
    Long patientId,
    Long diseaseId,
    String name,
    LocalDate date,
    String type,
    String note
) {
    public ExaminationDto withPatientId(long id) {
        return new ExaminationDto(
            id,
            diseaseId,
            name,
            date,
            type,
            note
        );
    }
}
