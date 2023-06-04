package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DiagnoseDto(
    Long id,
    @NotNull(message = "Patient must not be null")
    Long patientId,
    @NotNull(message = "Disease must not be null")
    DiseaseDto disease,
    @NotNull(message = "Date must not be null")
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
