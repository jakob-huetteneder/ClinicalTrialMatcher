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

import java.util.Random;

@Component
@Profile("generateData")
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
        return new Trial().setTitle(faker.battlefield1().weapon())
            .setStartDate(faker.date().birthday().toLocalDateTime().toLocalDate())
            .setEndDate(faker.date().birthday().toLocalDateTime().toLocalDate().plusWeeks(20))
            .setResearcher((Researcher) userDataGenerator.generateUser(Role.RESEARCHER))
            .setStudyType(faker.battlefield1().vehicle())
            .setBriefSummary(faker.lorem().maxLengthSentence(50))
            .setDetailedSummary(faker.lorem().paragraph())
            .setSponsor(faker.company().name())
            .setCollaborator(faker.company().name())
            .setStatus(TrialStatus.RECRUITING)
            .setLocation(faker.address().city())
            .setCrGender(faker.options().option(Gender.class))
            .setCrMinAge(faker.number().numberBetween(18, 50))
            .setCrMaxAge(faker.number().numberBetween(50, 100));


    }

}