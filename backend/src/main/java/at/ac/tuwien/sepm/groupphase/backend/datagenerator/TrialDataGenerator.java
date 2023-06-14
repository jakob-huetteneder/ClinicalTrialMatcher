package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Status;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Component
public class TrialDataGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Faker faker = new Faker(new Random(1));
    private final TrialRepository trialRepository;
    private final UserDataGenerator userDataGenerator;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public TrialDataGenerator(TrialRepository trialRepository, UserDataGenerator userDataGenerator,
                              ObjectMapper objectMapper, UserRepository userRepository) {
        this.trialRepository = trialRepository;
        this.userDataGenerator = userDataGenerator;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public void generateTrials() {
        generateTrials(10);
    }

    public void generateTrials(int amount) {
        for (int i = 0; i < amount; i++) {
            generateTrial();
        }
    }

    public void generateTrialsFor(Researcher researcher) {
        generateTrialsFor(researcher, 10);
    }

    public void generateTrialsFor(Researcher researcher, int amount) {
        for (int i = 0; i < amount; i++) {
            trialRepository.save(
                generateTrial(researcher)
            );
        }
    }


    public Trial generateTrial() {
        return generateTrial((Researcher) userDataGenerator.generateUser(Role.RESEARCHER));
    }

    public Trial generateTrial(Researcher researcher) {
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
            faker.options().option(TrialStatus.class),
            faker.address().city(),
            faker.options().option(Gender.class),
            faker.number().numberBetween(18, 50),
            faker.number().numberBetween(50, 100),
            new ArrayList<>(List.of(faker.lorem().sentence(), faker.lorem().sentence(), faker.lorem().sentence())),
            new ArrayList<>(List.of(faker.lorem().sentence(), faker.lorem().sentence(), faker.lorem().sentence())));
    }

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
        TrialStatus status,
        String location,
        Gender crGender,
        Integer crMinAge,
        Integer crMaxAge,
        List<String> inclusionCriteria,
        List<String> exclusionCriteria) {
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
            .setExclusionCriteria(exclusionCriteria));
    }

    public void parseTrialsFromJson() {
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
                    user = userDataGenerator.generateUser(trial.researcher().firstName(), trial.researcher().lastName(), trial.researcher().email(), Role.RESEARCHER, Status.ACTIVE);
                } else if (!(user instanceof Researcher)) {
                    throw new RuntimeException("User with email exists but is not a researcher " + trial.researcher().email() + " is not a researcher");
                }
                Researcher researcher = (Researcher) user;
                generateTrial(trial.title(), trial.startDate(), trial.endDate(), researcher, trial.studyType(), trial.briefSummary(), trial.detailedSummary(), trial.sponsor(), trial.collaborator(), trial.status(),
                    trial.location(), trial.crGender(), trial.crMinAge(), trial.crMaxAge(), trial.inclusionCriteria(), trial.exclusionCriteria());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}