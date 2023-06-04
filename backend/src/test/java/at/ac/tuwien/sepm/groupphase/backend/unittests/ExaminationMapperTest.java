package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.PatientDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ExaminationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
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
public class ExaminationMapperTest {

    @Autowired
    private PatientDataGenerator patientDataGenerator;
    @Autowired
    private ExaminationMapper examinationMapper;

    @Test
    public void testExaminationDtoToExamination() {
        Patient patient = patientDataGenerator.generatePatient();
        ExaminationDto examinationDto = new ExaminationDto(
            1L,
            patient.getId(),
            "test",
            LocalDate.of(2000,2,2),
            "",
            ""
        );

        Examination examination = examinationMapper.examinationDtoToExamination(examinationDto, examinationDto.patientId());

        assertAll(
            () -> assertEquals(examinationDto.id(), examination.getId()),
            () -> assertEquals(examinationDto.patientId(), examination.getPatient().getId()),
            () -> assertEquals(examinationDto.name(), examination.getName()),
            () -> assertEquals(examinationDto.date(), examination.getDate()),
            () -> assertEquals(examinationDto.type(), examination.getType()),
            () -> assertEquals(examinationDto.note(), examination.getNote())
        );
    }

    @Test
    public void testExaminationToExaminationDto() {

        Patient patient = new Patient()
            .setId(1L)
            .setFirstName("test")
            .setLastName("test")
            .setEmail("test")
            ;
        Examination examination = new Examination()
            .setId(1L)
            .setPatient(patient)
            .setName("test")
            .setDate(LocalDate.of(2000,2,2))
            .setType("")
            .setNote("")
            ;

        ExaminationDto examinationDto = examinationMapper.examinationToExaminationDto(examination);

        assertAll(
            () -> assertEquals(examinationDto.id(), examination.getId()),
            () -> assertEquals(examinationDto.patientId(), examination.getPatient().getId()),
            () -> assertEquals(examinationDto.name(), examination.getName()),
            () -> assertEquals(examinationDto.date(), examination.getDate()),
            () -> assertEquals(examinationDto.type(), examination.getType()),
            () -> assertEquals(examinationDto.note(), examination.getNote())
        );
    }
}
