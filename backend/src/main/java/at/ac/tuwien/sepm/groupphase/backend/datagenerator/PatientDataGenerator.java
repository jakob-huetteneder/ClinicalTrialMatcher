package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import net.datafaker.DateAndTime;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

            patientRepository.save(new Patient()
                .setEmail(faker.internet().password())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setBirthdate(faker.date().birthday().toLocalDateTime().toLocalDate())
                .setGender(gender));
        }
    }

    public Patient generatePatient() {
        return new Patient()
            .setEmail(faker.internet().password())
            .setFirstName(faker.name().firstName())
            .setLastName(faker.name().lastName())
            .setBirthdate(faker.date().birthday().toLocalDateTime().toLocalDate())
            .setGender(Gender.MALE);
    }

}
