package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;

import java.time.LocalDate;

public record DiagnoseDto(
    long id,
    long patientId,
    Disease disease,
    LocalDate date,
    String note
) {
    public DiagnoseDto withPatientId(long pid) {
        return new DiagnoseDto(
            id,
            pid,
            disease,
            date,
            note
        );
    }

    public DiagnoseDto withDiagnosisId(long id) {
        return new DiagnoseDto(
            id,
            patientId,
            disease,
            date,
            note
        );
    }
}
