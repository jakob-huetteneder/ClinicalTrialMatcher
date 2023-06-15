package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.datagenerator.UserDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Role;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TrialMapperTest {

    @Autowired
    private TrialMapper trialMapper;
    @Autowired
    private UserDataGenerator userDataGenerator;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testTrialDtoToTrial() {
        TrialDto trialDto = new TrialDto(
            1L,
            "title1",
            LocalDate.now(),
            LocalDate.now(),
            userMapper.applicationUserToUserDetailDto(userDataGenerator.generateUser(Role.RESEARCHER)),
            "studyType1",
            "brief1",
            "detail1",
            "sponsor1",
            "collaborator1",
            Trial.Status.RECRUITING,
            "location1",
            Gender.FEMALE,
            12,
            24,
            List.of("inclusion1"),
            List.of("exclusion1"));

        Trial mappedTrial = trialMapper.trialDtoToTrial(trialDto);

        assertAll(
            () -> assertEquals(trialDto.id(), mappedTrial.getId()),
            () -> assertEquals(trialDto.title(), mappedTrial.getTitle()),
            () -> assertEquals(trialDto.startDate(), mappedTrial.getStartDate()),
            () -> assertEquals(trialDto.endDate(), mappedTrial.getEndDate()),
            () -> assertEquals(trialDto.researcher().id(), mappedTrial.getResearcher().getId()),
            () -> assertEquals(trialDto.studyType(), mappedTrial.getStudyType()),
            () -> assertEquals(trialDto.briefSummary(), mappedTrial.getBriefSummary()),
            () -> assertEquals(trialDto.detailedSummary(), mappedTrial.getDetailedSummary()),
            () -> assertEquals(trialDto.sponsor(), mappedTrial.getSponsor()),
            () -> assertEquals(trialDto.collaborator(), mappedTrial.getCollaborator()),
            () -> assertEquals(trialDto.status(), mappedTrial.getStatus()),
            () -> assertEquals(trialDto.location(), mappedTrial.getLocation()),
            () -> assertEquals(trialDto.crGender(), mappedTrial.getCrGender()),
            () -> assertEquals(trialDto.crMinAge(), mappedTrial.getCrMinAge()),
            () -> assertEquals(trialDto.crMaxAge(), mappedTrial.getCrMaxAge()),
            () -> assertEquals(trialDto.inclusionCriteria(), mappedTrial.getInclusionCriteria()),
            () -> assertEquals(trialDto.exclusionCriteria(), mappedTrial.getExclusionCriteria())
        );
    }


    @Test
    public void testTrialToTrialDto() {
        // now do the same for trialToTrialDto as in testTrialDtoToTrial
        Trial trial = new Trial()
            .setId(1L)
            .setTitle("title1")
            .setStartDate(LocalDate.now())
            .setEndDate(LocalDate.now())
            .setResearcher(new Researcher())
            .setStudyType("studyType1")
            .setBriefSummary("brief1")
            .setDetailedSummary("detail1")
            .setSponsor("sponsor1")
            .setCollaborator("collaborator1")
            .setStatus(Trial.Status.RECRUITING)
            .setLocation("location1")
            .setCrGender(Gender.FEMALE)
            .setCrMinAge(13)
            .setCrMaxAge(25)
            .setInclusionCriteria(List.of("inclusion1"))
            .setExclusionCriteria(List.of("exclusion1"));

        TrialDto mappedTrialDto = trialMapper.trialToTrialDto(trial);

        assertAll(
            () -> assertEquals(trial.getId(), mappedTrialDto.id()),
            () -> assertEquals(trial.getTitle(), mappedTrialDto.title()),
            () -> assertEquals(trial.getStartDate(), mappedTrialDto.startDate()),
            () -> assertEquals(trial.getEndDate(), mappedTrialDto.endDate()),
            () -> assertEquals(trial.getResearcher().getId(), mappedTrialDto.researcher().id()),
            () -> assertEquals(trial.getStudyType(), mappedTrialDto.studyType()),
            () -> assertEquals(trial.getBriefSummary(), mappedTrialDto.briefSummary()),
            () -> assertEquals(trial.getDetailedSummary(), mappedTrialDto.detailedSummary()),
            () -> assertEquals(trial.getSponsor(), mappedTrialDto.sponsor()),
            () -> assertEquals(trial.getCollaborator(), mappedTrialDto.collaborator()),
            () -> assertEquals(trial.getStatus(), mappedTrialDto.status()),
            () -> assertEquals(trial.getLocation(), mappedTrialDto.location()),
            () -> assertEquals(trial.getCrGender(), mappedTrialDto.crGender()),
            () -> assertEquals(trial.getCrMinAge(), mappedTrialDto.crMinAge()),
            () -> assertEquals(trial.getCrMaxAge(), mappedTrialDto.crMaxAge()),
            () -> assertEquals(trial.getInclusionCriteria(), mappedTrialDto.inclusionCriteria()),
            () -> assertEquals(trial.getExclusionCriteria(), mappedTrialDto.exclusionCriteria())
        );

    }


}
