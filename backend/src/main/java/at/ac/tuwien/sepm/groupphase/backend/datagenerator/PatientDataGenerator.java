package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Random;

/**
 * This class generates representative patient data.
 */
@Component
public class PatientDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final PatientRepository patientRepository;
    private final UserDataGenerator userDataGenerator;

    public PatientDataGenerator(PatientRepository patientRepository, UserDataGenerator userDataGenerator) {
        this.patientRepository = patientRepository;
        this.userDataGenerator = userDataGenerator;
    }

    /**
     * Generates patient data and saves it to the database.
     */
    public void generatePatients() {
        LOG.trace("generatePatients()");
        for (int i = 0; i < 4; i++) {
            generatePatient();
        }
    }

    /**
     * Generates a single patient with random (faked) data.
     *
     * @return the generated patient
     */
    public Patient generatePatient() {
        LOG.trace("generatePatient()");
        return generatePatient(faker.options().option(Gender.class));
    }

    /**
     * Generates a single patient with random (faked) data. The gender is fixed.
     *
     * @param gender the gender of the patient
     * @return the generated patient
     */
    public Patient generatePatient(Gender gender) {
        LOG.trace("generatePatient()");
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

    /**
     * Generates a single patient with random (faked) data and an account.
     *
     * @return the generated patient
     */
    public Patient generatePatientWithAccount() {
        LOG.trace("generatePatientWithAccount()");

        Patient patient = generatePatient();
        ApplicationUser user = userDataGenerator.generateUser(
            patient.getFirstName(),
            patient.getLastName(),
            patient.getEmail(),
            Role.PATIENT,
            ApplicationUser.Status.ACTIVE
        );
        patient.setApplicationUser(user);
        return patientRepository.save(patient);
    }
}