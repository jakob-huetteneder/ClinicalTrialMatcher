package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Doctor;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class PatientMapper {


    public PatientMapper() {

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
            .setDiagnoses(patientDto.diagnoses())
            .setExaminations(patientDto.examinations());
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
            patient.getDiagnoses(),
            patient.getExaminations());
    }

    public List<Diagnose> patientDtoToDiagnose(PatientDto patientDto, Patient patient) {
        return patientDto.diagnoses() != null ? patientDto.diagnoses().stream().map(i -> new Diagnose()
            .setPatient(patient).setNote(i.getNote())
            .setDate(i.getDate()).setDisease(i.getDisease())).toList() : new LinkedList<>();
    }

    public List<Examination> patientDtoToExamination(PatientDto patientDto, Patient patient) {
        return patientDto.examinations() != null ? patientDto.examinations().stream().map(i -> new Examination()
            .setType(i.getType()).setName(i.getName()).setNote(i.getNote())
            .setPatient(patient).setDate(i.getDate()).setId(i.getId())).toList() : new LinkedList<>();
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
