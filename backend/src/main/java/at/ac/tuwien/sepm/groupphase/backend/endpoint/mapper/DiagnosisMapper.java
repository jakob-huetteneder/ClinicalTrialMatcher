package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Mapper for mapping {@link Diagnose} to {@link DiagnoseDto} and vice versa.
 */
@Component
public class DiagnosisMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final PatientRepository patientRepository;
    private final DiseaseMapper diseaseMapper;

    public DiagnosisMapper(PatientRepository patientRepository, DiseaseMapper diseaseMapper) {
        this.patientRepository = patientRepository;
        this.diseaseMapper = diseaseMapper;
    }

    /**
     * Converts a diagnosis DTO to a diagnosis entity.
     *
     * @param diagnoseDto the diagnosis DTO to be converted
     * @param patientId   the ID of the patient to be set
     * @return the converted diagnosis entity
     */
    public Diagnose diagnosisDtoToDiagnosis(DiagnoseDto diagnoseDto, Long patientId) {
        LOG.trace("diagnosisDtoToDiagnosis({}, {})", diagnoseDto, patientId);
        Optional<Patient> patient = patientId != null ? patientRepository.findById(patientId) : Optional.empty();
        return new Diagnose()
            .setId(diagnoseDto.id())
            .setPatient(patient.isPresent() ? patient.orElseThrow(() -> new IllegalArgumentException("Patient does not exist.")) : null)
            .setDisease(diseaseMapper.diseaseDtoToDisease(diagnoseDto.disease()))
            .setDate(diagnoseDto.date())
            .setNote(diagnoseDto.note());
    }

    /**
     * Converts a set of diagnosis DTOs to a set of diagnosis entities.
     *
     * @param diagnoses the set of diagnosis DTOs to be converted
     * @param patientId the ID of the patient to be set
     * @return the converted set of diagnosis entities
     */
    public Set<Diagnose> diagnosisDtoToDiagnosis(Set<DiagnoseDto> diagnoses, Long patientId) {
        LOG.trace("diagnosisDtoToDiagnosis({}, {})", diagnoses, patientId);
        Set<Diagnose> convertedDiagnoses = new HashSet<>();
        if (diagnoses != null) {
            for (DiagnoseDto diagnose : diagnoses) {
                convertedDiagnoses.add(diagnosisDtoToDiagnosis(diagnose, patientId));
            }
        }
        return convertedDiagnoses;
    }

    /**
     * Converts a diagnosis entity to a diagnosis DTO.
     *
     * @param diagnose the diagnosis entity to be converted
     * @return the converted diagnosis DTO
     */
    public DiagnoseDto diagnosisToDiagnosisDto(Diagnose diagnose) {
        LOG.trace("diagnosisToDiagnosisDto({})", diagnose);
        return new DiagnoseDto(
            diagnose.getId(),
            diagnose.getPatient() != null ? diagnose.getPatient().getId() : -1,
            diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease()),
            diagnose.getDate(),
            diagnose.getNote()
        );
    }

    /**
     * Converts a set of diagnosis entities to a set of diagnosis DTOs.
     *
     * @param diagnoses the set of diagnosis entities to be converted
     * @return the converted set of diagnosis DTOs
     */
    public Set<DiagnoseDto> diagnosisToDiagnosisDto(Set<Diagnose> diagnoses) {
        LOG.trace("diagnosisToDiagnosisDto({})", diagnoses);
        Set<DiagnoseDto> convertedDiagnoses = new HashSet<>();
        if (diagnoses != null) {
            for (Diagnose diagnose : diagnoses) {
                convertedDiagnoses.add(diagnosisToDiagnosisDto(diagnose));
            }
        }
        return convertedDiagnoses;
    }
}
