package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.springframework.stereotype.Component;

@Component
public class DiagnosisMapper {
    private final PatientRepository patientRepository;
    public DiagnosisMapper(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Diagnose diagnosisDtoToDiagnosis(DiagnoseDto diagnoseDto) {
        Patient patient = patientRepository.getReferenceById(diagnoseDto.patientId());
        return new Diagnose()
            .setId(diagnoseDto.id())
            .setPatient(patient)
            .setDisease(diagnoseDto.disease())
            .setDate(diagnoseDto.date())
            .setNote(diagnoseDto.note());
    }

    public DiagnoseDto diagnosisToDiagnosisDto(Diagnose diagnose) {
        return new DiagnoseDto(
            diagnose.getId(),
            diagnose.getPatient() != null ? diagnose.getPatient().getId() : null,
            diagnose.getDisease(),
            diagnose.getDate(),
            diagnose.getNote()
        );
    }

}
