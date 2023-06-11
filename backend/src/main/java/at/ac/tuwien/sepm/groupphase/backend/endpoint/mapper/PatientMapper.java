package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;

@Component
public class PatientMapper {

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

    public Patient patientDtoToPatient(PatientDto patientDto) {
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


    public PatientDto patientToPatientDto(Patient patient) {
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

    public PatientRequestDto patientToPatientRequestDto(Patient patient, Doctor doctor) {
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
