package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class PatientMapperTest {

    @Autowired
    private PatientMapper patientMapper;

    @Test
    public void testPatientToPatientDto() {
        Patient patient = new Patient().setFirstName("Max").setLastName("Mustermann")
            .setEmail("max@mustermann.com").setBirthdate(LocalDate.of(2000, 5, 21))
            .setAdmissionNote("Patient with severe abdominal pain...").setId(1L).setGender(Gender.MALE);

        PatientDto patientDto = patientMapper.patientToPatientDto(patient);

        assertAll(
            () -> assertEquals(patientDto.id(), patient.getId()),
            () -> assertEquals(patientDto.firstName(), patient.getFirstName()),
            () -> assertEquals(patientDto.lastName(), patient.getLastName()),
            () -> assertEquals(patientDto.email(), patient.getEmail()),
            () -> assertEquals(patientDto.admissionNote(), patient.getAdmissionNote()),
            () -> assertEquals(patientDto.birthdate(), patient.getBirthdate()),
            () -> assertEquals(patientDto.gender(), patient.getGender()),
            () -> assertEquals(patientDto.doctors(), patient.getDoctors()),
            () -> assertEquals(patientDto.examinations(), patient.getExaminations()),
            () -> assertEquals(patientDto.diagnoses(), patient.getDiagnoses()),
            () -> assertEquals(patientDto.applicationUser(), patient.getApplicationUser())
        );
    }

    @Test
    public void testPatientDtoToPatient() {
        Disease disease = new Disease().setName("Diabetes mellitus").setSynonyms("Zuckerkrankheit").setId(1L);
        Diagnose diagnose = new Diagnose().setDisease(disease).setNote("Diabolically fat.").setDate(LocalDate.now());
        Examination examination = new Examination().setType("Preliminary").setName("Examination 1")
            .setDate(LocalDate.now()).setNote("Blood test, ...").setId(1L);
        PatientDto patientDto = new PatientDto(1L, null, "Max", "Mustermann",
            "max@mustermann.com", "Patient with severe abdominal pain...",
            LocalDate.of(2000, 5, 21), Gender.MALE, null, Collections.singleton(diagnose),
            Collections.singleton(examination));

        Patient patient = patientMapper.patientDtoToPatient(patientDto);

        assertAll(
            () -> assertEquals(patientDto.id(), patient.getId()),
            () -> assertEquals(patientDto.firstName(), patient.getFirstName()),
            () -> assertEquals(patientDto.lastName(), patient.getLastName()),
            () -> assertEquals(patientDto.email(), patient.getEmail()),
            () -> assertEquals(patientDto.admissionNote(), patient.getAdmissionNote()),
            () -> assertEquals(patientDto.birthdate(), patient.getBirthdate()),
            () -> assertEquals(patientDto.gender(), patient.getGender()),
            () -> assertEquals(patientDto.doctors(), patient.getDoctors()),
            () -> assertEquals(patientDto.examinations(), patient.getExaminations()),
            () -> assertEquals(patientDto.diagnoses(), patient.getDiagnoses()),
            () -> assertEquals(patientDto.applicationUser(), patient.getApplicationUser()),
            () -> assertEquals(patientDto.diagnoses(), patient.getDiagnoses())
        );
    }

    @Test
    public void testPatientDtoToExamination() {
        Disease disease = new Disease().setName("Diabetes mellitus").setSynonyms("Zuckerkrankheit").setId(1L);
        Diagnose diagnose = new Diagnose().setDisease(disease).setNote("Diabolically fat.").setDate(LocalDate.now());
        Examination examination = new Examination().setType("Preliminary").setName("Examination 1")
            .setDate(LocalDate.now()).setNote("Blood test, ...").setId(1L);
        PatientDto patientDto = new PatientDto(1L, null, "Max", "Mustermann",
            "max@mustermann.com", "Patient with severe abdominal pain...",
            LocalDate.of(2000, 5, 21), Gender.MALE, null, Collections.singleton(diagnose),
            Collections.singleton(examination));

        Patient patient = patientMapper.patientDtoToPatient(patientDto);

        var examinations = patientMapper.patientDtoToExamination(patientDto, patient);

        assertEquals(1, examinations.size());

        var exam = examinations.get(0);

        assertAll(
            () -> assertEquals(exam.getId(), examination.getId()),
            () -> assertEquals(exam.getPatient(), patient),
            () -> assertEquals(exam.getDate(), examination.getDate()),
            () -> assertEquals(exam.getName(), examination.getName()),
            () -> assertEquals(exam.getNote(), examination.getNote()),
            () -> assertEquals(exam.getType(), examination.getType())
        );
    }

    @Test
    public void testPatientDtoToDiagnose() {
        Disease disease = new Disease().setName("Diabetes mellitus").setSynonyms("Zuckerkrankheit").setId(1L);
        Diagnose diagnose = new Diagnose().setDisease(disease).setNote("Diabolically fat.").setDate(LocalDate.now());
        Examination examination = new Examination().setType("Preliminary").setName("Examination 1")
            .setDate(LocalDate.now()).setNote("Blood test, ...").setId(1L);
        PatientDto patientDto = new PatientDto(1L, null, "Max", "Mustermann",
            "max@mustermann.com", "Patient with severe abdominal pain...",
            LocalDate.of(2000, 5, 21), Gender.MALE, null, Collections.singleton(diagnose),
            Collections.singleton(examination));

        Patient patient = patientMapper.patientDtoToPatient(patientDto);

        var diagnoses = patientMapper.patientDtoToDiagnose(patientDto, patient);

        assertEquals(1, diagnoses.size());

        var diag = diagnoses.get(0);

        assertAll(
            () -> assertEquals(diag.getId(), diagnose.getId()),
            () -> assertEquals(diag.getPatient(), patient),
            () -> assertEquals(diag.getDate(), diagnose.getDate()),
            () -> assertEquals(diag.getNote(), diagnose.getNote()),
            () -> assertEquals(diag.getDisease(), disease)
        );
    }
}
