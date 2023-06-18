package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class generates representative patient data.
 */
@Component
public class PatientDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final PatientRepository patientRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final PatientMapper patientMapper;
    private final ObjectMapper objectMapper;
    private final UserDataGenerator userDataGenerator;

    public PatientDataGenerator(PatientRepository patientRepository, ElasticsearchOperations elasticsearchOperations, PatientMapper patientMapper, ObjectMapper objectMapper, UserDataGenerator userDataGenerator) {
        this.patientRepository = patientRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.patientMapper = patientMapper;
        this.objectMapper = objectMapper;
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

        Patient patient = patientRepository.save(
            new Patient()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setBirthdate(faker.date().birthday()
                    .toLocalDateTime().toLocalDate())
                .setGender(gender)
        );

        elasticsearchOperations.save(patient);

        return patient;
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

    /**
     * Generates patient data from a JSON file and saves it to the database.
     */
    public void generatePatientsFromJson() {
        LOG.trace("generatePatientsFromJson()");

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("data/admission_notes.json");

        try {
            String[] admissionNotes = objectMapper.readValue(file, String[].class);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:5000/";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", MediaType.TEXT_PLAIN_VALUE);

            for (String admissionNote : admissionNotes) {
                LOG.info("Sending admission note: {}", admissionNote);
                HttpEntity<String> request = new HttpEntity<>(admissionNote, headers);

                ResponseEntity<String[]> diseasesResponse = restTemplate.postForEntity(url + "extract_entities", request, String[].class);
                String[] diseases = diseasesResponse.getBody();
                LOG.info("{} diseases found", diseases.length);

                Set<Diagnose> diagnoses = new HashSet<>();
                for (String disease : diseases) {
                    LOG.info("Disease: {}", disease);
                    Diagnose diagnose = new Diagnose()
                        .setDisease(
                            new Disease()
                                .setName(disease)
                        );
                    diagnoses.add(diagnose);
                }

                ResponseEntity<Integer> ageResponse = restTemplate.postForEntity(url + "extract_age", request, Integer.class);
                Integer age = ageResponse.getBody();
                LOG.info("Age: {}", age);

                ResponseEntity<String> genderResponse = restTemplate.postForEntity(url + "extract_gender", request, String.class);
                String genderString = genderResponse.getBody();
                LOG.info("Gender: {}", genderString);

                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

                Patient patient = new Patient()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setEmail(email)
                    .setAdmissionNote(admissionNote)
                    .setBirthdate(LocalDate.now().minusYears(age).plus(faker.random().nextInt(-100, 100), ChronoUnit.DAYS))
                    .setDiagnoses(diagnoses);
                if (genderString.equals("m")) {
                    patient.setGender(Gender.MALE);
                } else if (genderString.equals("f")) {
                    patient.setGender(Gender.FEMALE);
                } else {
                    patient.setGender(Gender.values()[faker.random().nextInt(0, 1)]);
                }

                patient = patientRepository.save(patient);
                elasticsearchOperations.save(patient);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}