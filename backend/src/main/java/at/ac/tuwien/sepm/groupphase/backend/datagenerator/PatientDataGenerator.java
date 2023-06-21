package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AdmissionNoteAnalyzerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final AdmissionNoteAnalyzerService admissionNoteAnalyzerService;

    public PatientDataGenerator(PatientRepository patientRepository, ElasticsearchOperations elasticsearchOperations, PatientMapper patientMapper,
                                ObjectMapper objectMapper, UserDataGenerator userDataGenerator, AdmissionNoteAnalyzerService admissionNoteAnalyzerService) {
        this.patientRepository = patientRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.patientMapper = patientMapper;
        this.objectMapper = objectMapper;
        this.userDataGenerator = userDataGenerator;
        this.admissionNoteAnalyzerService = admissionNoteAnalyzerService;
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

            LOG.info("Parsing {} admission notes", admissionNotes.length);

            for (String admissionNote : admissionNotes) {

                Integer age = admissionNoteAnalyzerService.extractAge(admissionNote);

                Gender gender = admissionNoteAnalyzerService.extractGender(admissionNote);
                if (gender == Gender.BOTH) {
                    gender = Gender.values()[faker.random().nextInt(0, 1)];
                }

                Set<DiseaseDto> diseases = admissionNoteAnalyzerService.extractDiseases(admissionNote);


                Set<Diagnose> diagnoses = diseases.stream().map(diseaseDto -> {
                    Disease disease = new Disease()
                        .setName(diseaseDto.name());
                    return new Diagnose()
                        .setDisease(disease);
                }).collect(Collectors.toSet());

                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com";

                Patient patient = new Patient()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setEmail(email)
                    .setAdmissionNote(admissionNote)
                    .setBirthdate(LocalDate.now().minusYears(age).plus(faker.random().nextInt(-100, 100), ChronoUnit.DAYS))
                    .setDiagnoses(diagnoses)
                    .setGender(gender);

                patient = patientRepository.save(patient);
                elasticsearchOperations.save(patient);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}