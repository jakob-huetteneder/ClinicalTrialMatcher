package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiagnoseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiagnosisMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
public class DiagnoseMapperTest {

    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private DiagnosisMapper diagnosisMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;

    @Test
    public void testDiagnoseDtoToDiagnose() {
        Patient patient = patientDataGenerator.generatePatient();
        DiagnoseDto diagnoseDto = new DiagnoseDto(
            1L,
            patient.getId(),
            null,
            LocalDate.of(2000,2,2),
            ""
        );

        Diagnose diagnose = diagnosisMapper.diagnosisDtoToDiagnosis(diagnoseDto, diagnoseDto.patientId());

        assertAll(
            () -> assertEquals(diagnoseDto.id(), diagnose.getId()),
            () -> assertEquals(diagnoseDto.patientId(), diagnose.getPatient().getId()),
            () -> assertEquals(diagnoseDto.disease(), diseaseMapper.diseaseToDiseaseDto(diagnose.getDisease())),
            () -> assertEquals(diagnoseDto.date(), diagnose.getDate()),
            () -> assertEquals(diagnoseDto.note(), diagnose.getNote())
        );
    }

    @Test
    public void testDiagnoseToDiagnoseDto() {

        Patient patient = new Patient()
            .setId(1L)
            .setFirstName("test")
            .setLastName("test")
            .setEmail("test")
            ;

        Disease disease = new Disease()
            .setId(1L)
            .setName("test")
            .setSynonyms("fest")
            ;
        Diagnose diagnose = new Diagnose()
            .setId(1L)
            .setPatient(patient)
            .setDisease(disease)
            .setDate(LocalDate.of(2000,2,2))
            .setNote("")
            ;

        DiagnoseDto diagnoseDto = diagnosisMapper.diagnosisToDiagnosisDto(diagnose);

        assertAll(
            () -> assertEquals(diagnoseDto.id(), diagnose.getId()),
            () -> assertEquals(diagnoseDto.patientId(), diagnose.getPatient().getId()),
            () -> assertEquals(diagnoseDto.disease().name(), diagnose.getDisease().getName()),
            () -> assertEquals(diagnoseDto.date(), diagnose.getDate()),
            () -> assertEquals(diagnoseDto.note(), diagnose.getNote())
        );
    }
}
