package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ExaminationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ExaminationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateData")
public class ExaminationDataGenerator {

    private static final Faker faker = new Faker(new Random(1));
    private final ExaminationRepository examinationRepository;
    private final ExaminationMapper examinationMapper;

    private ExaminationDataGenerator(ExaminationRepository examinationRepository, ExaminationMapper examinationMapper) {
        this.examinationRepository = examinationRepository;
        this.examinationMapper = examinationMapper;
    }

    @PostConstruct
    public void generateExaminations() {
        for (int i = 0; i <= 4; i++) {
            examinationRepository.save(new Examination()
                .setPatient(null)
                .setName(faker.name().name())
                .setNote(faker.funnyName().name())
                .setType(faker.funnyName().name())
                .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
            );
        }
    }

    public Examination generateExamination() {
        return new Examination()
            .setPatient(null)
            .setName(faker.name().name())
            .setNote(faker.funnyName().name())
            .setType(faker.funnyName().name())
            .setDate(faker.date().birthday().toLocalDateTime().toLocalDate())
            ;
    }

    public Examination generateExaminationfromDTO(ExaminationDto examinationDto) {
        return examinationMapper.patientExaminationDtotoExamination(examinationDto);
    }
}
