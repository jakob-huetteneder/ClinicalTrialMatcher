package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AdmissionNoteAnalyzerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class generates representative trial data.
 */
@Component
public class TrialDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Faker faker = new Faker(new Random(1));
    private final TrialRepository trialRepository;
    private final UserDataGenerator userDataGenerator;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final AdmissionNoteAnalyzerService admissionNoteAnalyzerService;
    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;

    public TrialDataGenerator(TrialRepository trialRepository, UserDataGenerator userDataGenerator,
                              ObjectMapper objectMapper, UserRepository userRepository,
                              AdmissionNoteAnalyzerService admissionNoteAnalyzerService, DiseaseRepository diseaseRepository,
                              DiseaseMapper diseaseMapper) {
        this.trialRepository = trialRepository;
        this.userDataGenerator = userDataGenerator;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.admissionNoteAnalyzerService = admissionNoteAnalyzerService;
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
    }

    /**
     * Generates trial data and saves it to the database.
     */
    public void generateTrials() {
        LOG.trace("generateTrials()");
        generateTrials(10);
    }

    /**
     * Generates a number of trials.
     *
     * @param amount the number of trials to generate
     */
    public void generateTrials(int amount) {
        LOG.trace("generateTrials({})", amount);
        for (int i = 0; i < amount; i++) {
            generateTrial();
        }
    }

    /**
     * Generates trials for a researcher.
     *
     * @param researcher the researcher to generate trials for
     */
    public void generateTrialsFor(Researcher researcher) {
        LOG.trace("generateTrialsFor({})", researcher);
        generateTrialsFor(researcher, 10);
    }

    /**
     * Generates a number of trials for a researcher.
     *
     * @param researcher the researcher to generate trials for
     * @param amount     the number of trials to generate
     */
    public void generateTrialsFor(Researcher researcher, int amount) {
        LOG.trace("generateTrialsFor({}, {})", researcher, amount);
        for (int i = 0; i < amount; i++) {
            trialRepository.save(
                generateTrial(researcher)
            );
        }
    }

    /**
     * Generates a single trial with random (faked) data.
     *
     * @return the generated trial
     */
    public Trial generateTrial() {
        LOG.trace("generateTrial()");
        return generateTrial((Researcher) userDataGenerator.generateUser(Role.RESEARCHER));
    }

    /**
     * Generates a single trial with random (faked) data for a fixed researcher.
     *
     * @param researcher the researcher to generate the trial for
     * @return the generated trial
     */
    public Trial generateTrial(Researcher researcher) {
        LOG.trace("generateTrial({})", researcher);
        LocalDate startDate = faker.date().birthday().toLocalDateTime().toLocalDate();
        LocalDate endDate = startDate.plusMonths(faker.number().numberBetween(2, 24));
        return generateTrial(
            faker.book().title(),
            startDate,
            endDate,
            researcher,
            faker.options().option("Interventional", "Observational"),
            faker.lorem().maxLengthSentence(50),
            faker.lorem().paragraph(),
            faker.company().name(),
            faker.company().name(),
            faker.options().option(Trial.Status.class),
            faker.address().city(),
            faker.options().option(Gender.class),
            faker.number().numberBetween(18, 50),
            faker.number().numberBetween(50, 100),
            new ArrayList<>(List.of(faker.lorem().sentence(), faker.lorem().sentence(), faker.lorem().sentence())),
            new ArrayList<>(List.of(faker.lorem().sentence(), faker.lorem().sentence(), faker.lorem().sentence())),
            new ArrayList<>(List.of(new Disease().setName(faker.lorem().word()))));
    }

    /**
     * Generates a single trial with fixed data.
     *
     * @param title             the title of the trial
     * @param startDate         the start date of the trial
     * @param endDate           the end date of the trial
     * @param researcher        the researcher to generate the trial for
     * @param studyType         the study type of the trial
     * @param briefSummary      the brief summary of the trial
     * @param detailedSummary   the detailed summary of the trial
     * @param sponsor           the sponsor of the trial
     * @param collaborator      the collaborator of the trial
     * @param status            the status of the trial
     * @param location          the location of the trial
     * @param crGender          the required gender of participants
     * @param crMinAge          the minimum age of participants
     * @param crMaxAge          the maximum age of participants
     * @param inclusionCriteria the inclusion criteria of the trial
     * @param exclusionCriteria the exclusion criteria of the trial
     * @param diseases          the diseases of the trial
     * @return the generated trial
     */
    public Trial generateTrial(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        Researcher researcher,
        String studyType,
        String briefSummary,
        String detailedSummary,
        String sponsor,
        String collaborator,
        Trial.Status status,
        String location,
        Gender crGender,
        Integer crMinAge,
        Integer crMaxAge,
        List<String> inclusionCriteria,
        List<String> exclusionCriteria,
        List<Disease> diseases) {
        LOG.trace("generateTrial({}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {})",
            title, startDate, endDate, researcher, studyType, briefSummary, detailedSummary, sponsor, collaborator,
            status, location, crGender, crMinAge, crMaxAge, inclusionCriteria, exclusionCriteria, diseases);
        diseaseRepository.saveAll(diseases);
        return trialRepository.save(new Trial()
            .setTitle(title)
            .setStartDate(startDate)
            .setEndDate(endDate)
            .setResearcher(researcher)
            .setStudyType(studyType)
            .setBriefSummary(briefSummary)
            .setDetailedSummary(detailedSummary)
            .setSponsor(sponsor)
            .setCollaborator(collaborator)
            .setStatus(status)
            .setLocation(location)
            .setCrGender(crGender)
            .setCrMinAge(crMinAge)
            .setCrMaxAge(crMaxAge)
            .setInclusionCriteria(inclusionCriteria)
            .setExclusionCriteria(exclusionCriteria)
            .setDiseases(diseases));
    }

    /**
     * Parses trials from a json resource file and saves them to the database.
     */
    @Transactional
    public void parseTrialsFromJson() {
        LOG.trace("parseTrialsFromJson()");
        // get json from resources/data/trials.json
        // parse json to list of trials
        // save trials to database
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream file = classLoader.getResourceAsStream("data/trials.json");

        try {
            TrialDto[] trials = objectMapper.readValue(file, TrialDto[].class);
            LOG.info("Parsing {} trials", trials.length);
            for (TrialDto trial : trials) {
                ApplicationUser user = userRepository.findByEmail(trial.researcher().email());
                if (user == null) {
                    user = userDataGenerator.generateUser(trial.researcher().firstName(), trial.researcher().lastName(), trial.researcher().email(), Role.RESEARCHER, ApplicationUser.Status.ACTIVE);
                } else if (!(user instanceof Researcher)) {
                    throw new RuntimeException("User with email exists but is not a researcher " + trial.researcher().email() + " is not a researcher");
                }
                Researcher researcher = (Researcher) user;
                List<Disease> diseases = admissionNoteAnalyzerService.extractDiseases(trial.briefSummary() + " " + trial.detailedSummary()).stream().map(diseaseMapper::diseaseDtoToDisease).toList();
                List<Disease> toSave = new ArrayList<>();
                for (Disease disease : diseases) {
                    List<Disease> storedDiseases = diseaseRepository.findDiseasesByName(disease.getName());

                    if (storedDiseases.isEmpty()) {
                        toSave.add(diseaseRepository.save(disease));
                    } else {
                        toSave.add(storedDiseases.get(0));
                    }
                }

                generateTrial(trial.title(), trial.startDate(), trial.endDate(), researcher, trial.studyType(), trial.briefSummary(), trial.detailedSummary(), trial.sponsor(), trial.collaborator(), trial.status(),
                    trial.location(), trial.crGender(), trial.crMinAge(), trial.crMaxAge(), trial.inclusionCriteria(), trial.exclusionCriteria(), toSave);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}