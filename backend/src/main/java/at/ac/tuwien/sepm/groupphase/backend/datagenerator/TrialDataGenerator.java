package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("generateTrials")
public class TrialDataGenerator {
    private static final Faker faker = new Faker(new Random(1));
    private final TrialRepository trialRepository;

    private final UserDataGenerator userDataGenerator;

    public TrialDataGenerator(TrialRepository trialRepository, UserDataGenerator userDataGenerator) {
        this.trialRepository = trialRepository;
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    public void generateTrials() {

        for (int i = 0; i < 10; i++) {
            trialRepository.save(
                generateTrial()
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
            faker.lorem().paragraph());
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
        String crFreeText) {
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
            .setCrFreeText(crFreeText));
    }

}