package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.springframework.stereotype.Component;

@Component
public class DiagnosisMapper {
    private final PatientRepository patientRepository;
    private final DiseaseMapper diseaseMapper;

    public DiagnosisMapper(PatientRepository patientRepository, DiseaseMapper diseaseMapper) {
        this.patientRepository = patientRepository;
        this.diseaseMapper = diseaseMapper;
    }

    public Diagnose diagnosisDtoToDiagnosis(DiagnoseDto diagnoseDto) {
        Patient patient = patientRepository.getReferenceById(diagnoseDto.patientId());
        return new Diagnose()
            .setId(diagnoseDto.id())
            .setPatient(patient)
            .setDisease(diseaseMapper.diseaseDtoToDisease(diagnoseDto.disease()))
            .setDate(diagnoseDto.date())
            .setNote(diagnoseDto.note());
    }

    public DiagnoseDto diagnosisToDiagnosisDto(Diagnose diagnose) {
        return new DiagnoseDto(
            diagnose.getId(),
            diagnose.getPatient() != null ? diagnose.getPatient().getId() : -1,
            diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease()),
            diagnose.getDate(),
            diagnose.getNote()
        );
    }

}
