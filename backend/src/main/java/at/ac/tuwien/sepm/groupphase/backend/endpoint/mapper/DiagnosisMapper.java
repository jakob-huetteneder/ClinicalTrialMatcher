package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DiagnosisMapper {
    private final PatientRepository patientRepository;
    private final DiseaseMapper diseaseMapper;

    public DiagnosisMapper(PatientRepository patientRepository, DiseaseMapper diseaseMapper) {
        this.patientRepository = patientRepository;
        this.diseaseMapper = diseaseMapper;
    }

    public Diagnose diagnosisDtoToDiagnosis(DiagnoseDto diagnoseDto, Long patientId) {
        Optional<Patient> patient = patientId != null ? patientRepository.findById(patientId) : Optional.empty();
        return new Diagnose()
            .setId(diagnoseDto.id())
            .setPatient(patient.isPresent() ? patient.orElseThrow(() -> new IllegalArgumentException("Patient does not exist.")) : null)
            .setDisease(diseaseMapper.diseaseDtoToDisease(diagnoseDto.disease()))
            .setDate(diagnoseDto.date())
            .setNote(diagnoseDto.note());
    }

    public Set<Diagnose> diagnosisDtoToDiagnosis(Set<DiagnoseDto> diagnoses, Long patientId) {
        Set<Diagnose> convertedDiagnoses = new HashSet<>();
        if (diagnoses != null) {
            for (DiagnoseDto diagnose : diagnoses) {
                convertedDiagnoses.add(diagnosisDtoToDiagnosis(diagnose, patientId));
            }
        }
        return convertedDiagnoses;
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

    public Set<DiagnoseDto> diagnosisToDiagnosisDto(Set<Diagnose> diagnoses) {
        Set<DiagnoseDto> convertedDiagnoses = new HashSet<>();
        if (diagnoses != null) {
            for (Diagnose diagnose : diagnoses) {
                convertedDiagnoses.add(diagnosisToDiagnosisDto(diagnose));
            }
        }
        return convertedDiagnoses;
    }
}
