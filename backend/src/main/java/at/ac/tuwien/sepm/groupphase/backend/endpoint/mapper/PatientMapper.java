package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class PatientMapper {

    private final UserMapper userMapper;
    private final DiagnosisMapper diagnosisMapper;
    private final ExaminationMapper examinationMapper;

    public PatientMapper(UserMapper userMapper, DiagnosisMapper diagnosisMapper, ExaminationMapper examinationMapper) {
        this.userMapper = userMapper;
        this.diagnosisMapper = diagnosisMapper;
        this.examinationMapper = examinationMapper;
    }

    public Patient patientDtoToPatient(PatientDto patientDto) {
        return new Patient()
            .setId(patientDto.id())
            .setApplicationUser(patientDto.applicationUser())
            .setFirstName(patientDto.firstName())
            .setLastName(patientDto.lastName())
            .setEmail(patientDto.email())
            .setAdmissionNote(patientDto.admissionNote())
            .setBirthdate(patientDto.birthdate())
            .setGender(patientDto.gender())
            .setDoctors(userMapper.userDetailDtoToApplicationUser(patientDto.doctors()).stream().map(user -> {
                if (user instanceof Doctor) {
                    return (Doctor) user;
                }
                return null;
            }).collect(Collectors.toSet()))
            .setDiagnoses(patientDto.id() != null ? diagnosisMapper.diagnosisDtoToDiagnosis(patientDto.diagnoses(), patientDto.id()) : new HashSet<>())
            .setExaminations(patientDto.id() != null ? examinationMapper.examinationDtoToExamination(patientDto.examinations(), patientDto.id()) : new HashSet<>());
    }


    public PatientDto patientToPatientDto(Patient patient) {
        return new PatientDto(patient.getId(),
            patient.getApplicationUser(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getEmail(),
            patient.getAdmissionNote(),
            patient.getBirthdate(),
            patient.getGender(),
            patient.getDoctors() == null ? new HashSet<>() : patient.getDoctors().stream().map(userMapper::applicationUserToUserDetailDto).collect(Collectors.toSet()),
            diagnosisMapper.diagnosisToDiagnosisDto(patient.getDiagnoses()),
            examinationMapper.examinationToExaminationDto(patient.getExaminations()));
    }
}
