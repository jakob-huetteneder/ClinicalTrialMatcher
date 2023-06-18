package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Objects;

/**
 * Mapper for mapping {@link Patient} to {@link PatientDto} and vice versa.
 * Also maps {@link Patient} and {@link Doctor} to {@link PatientRequestDto}.
 */
@Component
public class PatientMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserMapper userMapper;
    private final DiagnosisMapper diagnosisMapper;
    private final ExaminationMapper examinationMapper;
    private final TreatsMapper treatsMapper;

    public PatientMapper(UserMapper userMapper, DiagnosisMapper diagnosisMapper, ExaminationMapper examinationMapper, TreatsMapper treatsMapper) {
        this.userMapper = userMapper;
        this.diagnosisMapper = diagnosisMapper;
        this.examinationMapper = examinationMapper;
        this.treatsMapper = treatsMapper;
    }

    /**
     * Converts a patient DTO to a patient entity.
     *
     * @param patientDto the patient DTO to be converted
     * @return the converted patient entity
     */
    public Patient patientDtoToPatient(PatientDto patientDto) {
        LOG.trace("patientDtoToPatient({})", patientDto);
        return new Patient()
            .setId(patientDto.id())
            .setApplicationUser(patientDto.applicationUser() != null ? userMapper.userDetailDtoToApplicationUser(patientDto.applicationUser()) : null)
            .setFirstName(patientDto.firstName())
            .setLastName(patientDto.lastName())
            .setEmail(patientDto.email())
            .setAdmissionNote(patientDto.admissionNote())
            .setBirthdate(patientDto.birthdate())
            .setGender(patientDto.gender())
            .setTreats(null)
            .setDiagnoses(patientDto.diagnoses() != null ? diagnosisMapper.diagnosisDtoToDiagnosis(patientDto.diagnoses(), patientDto.id()) : new HashSet<>())
            .setExaminations(patientDto.examinations() != null ? examinationMapper.examinationDtoToExamination(patientDto.examinations(), patientDto.id()) : new HashSet<>());
    }

    /**
     * Converts a patient entity to a patient DTO.
     *
     * @param patient the patient entity to be converted
     * @return the converted patient DTO
     */
    public PatientDto patientToPatientDto(Patient patient) {
        LOG.trace("patientToPatientDto({})", patient);
        return new PatientDto(patient.getId(),
            patient.getApplicationUser() == null ? null : userMapper.applicationUserToUserDetailDto(patient.getApplicationUser()),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getEmail(),
            patient.getAdmissionNote(),
            patient.getBirthdate(),
            patient.getGender(),
            diagnosisMapper.diagnosisToDiagnosisDto(patient.getDiagnoses()),
            examinationMapper.examinationToExaminationDto(patient.getExaminations()));
    }

    /**
     * Converts a patient entity to a patient request DTO.
     *
     * @param patient the patient entity to be converted
     * @param doctor  the doctor entity to be converted
     * @return the converted patient request DTO
     */
    public PatientRequestDto patientToPatientRequestDto(Patient patient, Doctor doctor) {
        LOG.trace("patientToPatientRequestDto({}, {})", patient, doctor);
        return new PatientRequestDto(
            patient.getId(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getBirthdate(),
            patient.getGender(),
            treatsMapper.treatsToTreatsDto(patient.getTreats().stream().filter(treats -> Objects.equals(treats.getDoctor().getId(), doctor.getId())).findFirst().orElse(null))
        );
    }
}
