package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.repository.TrialListRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class TrialListDataGenerator {
    private static final Faker faker = new Faker(new Random(1));
    private final TrialListRepository trialListRepository;
    private final UserDataGenerator userDataGenerator;


    public TrialListDataGenerator(TrialListRepository trialListRepository, UserDataGenerator userDataGenerator) {
        this.trialListRepository = trialListRepository;
        this.userDataGenerator = userDataGenerator;
    }

    public void generateTrialLists() {
        generateTrialLists(10);
    }



    public void generateTrialLists(int amount) {
        for (int i = 0; i < amount; i++) {
            generateTrialList();
        }
    }

    public void generateTrialListsFor(ApplicationUser user) {
        generateTrialListsFor(user, 10);
    }

    public void generateTrialListsFor(ApplicationUser user, int amount) {
        for (int i = 0; i < amount; i++) {
            trialListRepository.save(
                generateTrialList(user)
            );
        }
    }

    public TrialList generateTrialList() {
        return generateTrialList(userDataGenerator.generateUser(Role.RESEARCHER));
    }


    public TrialList generateTrialList(ApplicationUser user) {
        return generateTrialList(
            faker.book().title(),
            user,
            new ArrayList<>()
        );
    }

    public TrialList generateTrialList(String name, ApplicationUser user, List<Trial> trials) {
        return trialListRepository.save(new TrialList().setName(name)
            .setUser(user).setTrial(trials));

    }







}
