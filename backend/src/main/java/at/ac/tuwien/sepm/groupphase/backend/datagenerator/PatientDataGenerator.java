package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PatientDataGenerator {
    private static final Faker faker = new Faker(new Random(1));
    private final PatientRepository patientRepository;
    private final UserDataGenerator userDataGenerator;

    public PatientDataGenerator(PatientRepository patientRepository, UserDataGenerator userDataGenerator) {
        this.patientRepository = patientRepository;
        this.userDataGenerator = userDataGenerator;
    }

    public void generatePatients() {
        for (int i = 0; i < 4; i++) {
            generatePatient();
        }
    }

    public Patient generatePatient() {
        return generatePatient(faker.options().option(Gender.class));
    }

    public Patient generatePatient(Gender gender) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

        return patientRepository.save(
            new Patient()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setBirthdate(faker.date().birthday()
                    .toLocalDateTime().toLocalDate())
                .setGender(gender)
        );
    }

    public Patient generatePatientWithAccount() {
        Patient patient = generatePatient();
        ApplicationUser user = userDataGenerator.generateUser(
            patient.getFirstName(),
            patient.getLastName(),
            patient.getEmail(),
            Role.PATIENT,
            Status.ACTIVE
        );
        patient.setApplicationUser(user);
        return patientRepository.save(patient);
    }
}