package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.DiagnosisDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.ExaminationDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiagnosisMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ExaminationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
public class PatientMapperTest {

    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private DiagnosisDataGenerator diagnosisDataGenerator;
    @Autowired
    private ExaminationDataGenerator examinationDataGenerator;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private DiagnosisMapper diagnosisMapper;
    @Autowired
    private ExaminationMapper examinationMapper;

    @Test
    public void testPatientToPatientDto() {
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        Examination examination = examinationDataGenerator.generateExamination();
        Patient patient = new Patient().setFirstName("Max").setLastName("Mustermann")
            .setEmail("max@mustermann.com").setBirthdate(LocalDate.of(2000, 5, 21))
            .setAdmissionNote("Patient with severe abdominal pain...").setId(1L).setGender(Gender.MALE)
            .setDiagnoses(Collections.singleton(diagnose)).setExaminations(Collections.singleton(examination));

        PatientDto patientDto = patientMapper.patientToPatientDto(patient);

        assertAll(
            () -> assertEquals(patientDto.id(), patient.getId()),
            () -> assertEquals(patientDto.firstName(), patient.getFirstName()),
            () -> assertEquals(patientDto.lastName(), patient.getLastName()),
            () -> assertEquals(patientDto.email(), patient.getEmail()),
            () -> assertEquals(patientDto.admissionNote(), patient.getAdmissionNote()),
            () -> assertEquals(patientDto.birthdate(), patient.getBirthdate()),
            () -> assertEquals(patientDto.gender(), patient.getGender()),
            () -> assertEquals(patientDto.examinations(), examinationMapper.examinationToExaminationDto(patient.getExaminations())),
            () -> assertEquals(patientDto.diagnoses(), diagnosisMapper.diagnosisToDiagnosisDto(patient.getDiagnoses())),
            () -> assertEquals(patientDto.applicationUser(), patient.getApplicationUser())
        );
    }

    @Test
    public void testPatientDtoToPatient() {
        Diagnose diagnose = diagnosisDataGenerator.generateDiagnose();
        Examination examination = examinationDataGenerator.generateExamination();
        Patient storedPatient = patientDataGenerator.generatePatient();
        diagnose.setPatient(storedPatient);
        examination.setPatient(storedPatient);
        PatientDto patientDto = new PatientDto(storedPatient.getId(), null, "Max", "Mustermann",
            "max@mustermann.com", "Patient with severe abdominal pain...",
            LocalDate.of(2000, 5, 21), Gender.MALE,
            Collections.singleton(diagnosisMapper.diagnosisToDiagnosisDto(diagnose)),
            Collections.singleton(examinationMapper.examinationToExaminationDto(examination)));

        Patient patient = patientMapper.patientDtoToPatient(patientDto);

        assertAll(
            () -> assertEquals(patientDto.id(), patient.getId()),
            () -> assertEquals(patientDto.firstName(), patient.getFirstName()),
            () -> assertEquals(patientDto.lastName(), patient.getLastName()),
            () -> assertEquals(patientDto.email(), patient.getEmail()),
            () -> assertEquals(patientDto.admissionNote(), patient.getAdmissionNote()),
            () -> assertEquals(patientDto.birthdate(), patient.getBirthdate()),
            () -> assertEquals(patientDto.gender(), patient.getGender()),
            () -> assertEquals(patientDto.examinations(), examinationMapper.examinationToExaminationDto(patient.getExaminations())),
            () -> assertEquals(patientDto.diagnoses(), diagnosisMapper.diagnosisToDiagnosisDto(patient.getDiagnoses())),
            () -> assertEquals(patientDto.applicationUser(), patient.getApplicationUser())
        );
    }
}
