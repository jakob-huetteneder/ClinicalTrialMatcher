package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Researcher;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TrialMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class TrialMapperTest {

    @Autowired
    private TrialMapper trialMapper;


    @Test
    public void test() {
        assertEquals(1, 1);
    }

    @Test
    public void testTrialDtoToTrial() {
        TrialDto trialDto = new TrialDto(
            1L,
            "title1",
            LocalDate.now(),
            LocalDate.now(),
            new Researcher(),
            "studyType1",
            "brief1",
            "detail1",
            "sponsor1",
            "collaborator1",
            TrialStatus.RECRUITING,
            "location1",
            Gender.FEMALE,
            12,
            24,
            "inclusion1");

        Trial mappedTrial = trialMapper.trialDtoToTrial(trialDto);

        assertAll(
            () -> assertEquals(trialDto.id(), mappedTrial.getId()),
            () -> assertEquals(trialDto.title(), mappedTrial.getTitle()),
            () -> assertEquals(trialDto.startDate(), mappedTrial.getStartDate()),
            () -> assertEquals(trialDto.endDate(), mappedTrial.getEndDate()),
            () -> assertEquals(trialDto.researcher(), mappedTrial.getResearcher()),
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
            () -> assertEquals(trialDto.crFreeText(), mappedTrial.getCrFreeText())
        );
    }


    @Test
    public void trialToTrialDto() {
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
            .setStatus(TrialStatus.RECRUITING)
            .setLocation("location1")
            .setCrGender(Gender.FEMALE)
            .setCrMinAge(13)
            .setCrMaxAge(25)
            .setCrFreeText("inclusion1");

        TrialDto mappedTrialDto = trialMapper.trialToTrialDto(trial);

        assertAll(
            () -> assertEquals(trial.getId(), mappedTrialDto.id()),
            () -> assertEquals(trial.getTitle(), mappedTrialDto.title()),
            () -> assertEquals(trial.getStartDate(), mappedTrialDto.startDate()),
            () -> assertEquals(trial.getEndDate(), mappedTrialDto.endDate()),
            () -> assertEquals(trial.getResearcher(), mappedTrialDto.researcher()),
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
            () -> assertEquals(trial.getCrFreeText(), mappedTrialDto.crFreeText())
        );

    }


}
