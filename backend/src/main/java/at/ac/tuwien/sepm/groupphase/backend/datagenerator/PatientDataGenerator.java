package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("generateData")
public class PatientDataGenerator {
    private static final Faker faker = new Faker(new Random(1));
    private final PatientRepository patientRepository;

    public PatientDataGenerator(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @PostConstruct
    public void generatePatients() {
        for (int i = 0; i < 4; i++) {
            Gender gender = i % 2 == 0 ? Gender.FEMALE : Gender.MALE;
            generatePatient(gender);
        }
    }

    public Patient generatePatient() {
        return generatePatient(Gender.MALE);
    }

    public Patient generatePatient(Gender gender) {
        return patientRepository.save(
            new Patient()
                .setEmail(faker.internet().password())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setBirthdate(faker.date().birthday()
                    .toLocalDateTime().toLocalDate())
                .setGender(gender)
        );
    }
}