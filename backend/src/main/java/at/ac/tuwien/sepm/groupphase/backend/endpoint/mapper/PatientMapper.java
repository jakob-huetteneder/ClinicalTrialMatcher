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

    private final DiagnosisMapper diagnosisMapper;
    private final ExaminationMapper examinationMapper;

    public PatientMapper(DiagnosisMapper diagnosisMapper, ExaminationMapper examinationMapper) {
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
            .setTreats(null)
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
            patient.getTreats().stream().filter(treats -> Objects.equals(treats.getDoctor().getId(), doctor.getId())).findFirst().orElse(null)
        );
    }
}
