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
            patientRepository.save(
                generatePatient()
            );
        }
    }

    public Patient generatePatient() {
        return new Patient()
            .setFirstName(faker.name().firstName())
            .setLastName(faker.name().lastName())
            .setEmail(faker.internet().emailAddress())
            .setAdmissionNote(faker.lorem().paragraph())
            .setBirthdate(faker.date().birthday().toLocalDateTime().toLocalDate())
            .setGender(Gender.values()[faker.number().numberBetween(0, 2)]);
    }
}
