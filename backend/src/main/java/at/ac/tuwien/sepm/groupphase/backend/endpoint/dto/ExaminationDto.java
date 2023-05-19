package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDate;

public record ExaminationDto(
    Long id,
    Long patientId,
    String name,
    LocalDate date,
    String type,
    String note
) {
    public ExaminationDto withPatientId(long pid) {
        return new ExaminationDto(
            id,
            pid,
            name,
            date,
            type,
            note
        );
    }

    public ExaminationDto withExaminationId(long id) {
        return new ExaminationDto(
            id,
            patientId,
            name,
            date,
            type,
            note
        );
    }
}
