package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.springframework.stereotype.Component;

@Component
public class TrialMapper {

    public Trial trialDtoToTrial(TrialDto trialDto) {
        return new Trial()
            .setId(trialDto.id())
            .setCollaborator(trialDto.collaborator())
            .setBriefSummary(trialDto.briefSummary())
            .setCrMaxAge(trialDto.crMaxAge())
            .setCrGender(trialDto.crGender())
            .setCrFreeText(trialDto.crFreeText())
            .setCrMinAge(trialDto.crMinAge())
            .setTitle(trialDto.title())
            .setEndDate(trialDto.endDate())
            .setStartDate(trialDto.startDate())
            .setDetailedSummary(trialDto.detailedSummary())
            .setResearcher(trialDto.researcher())
            .setSponsor(trialDto.sponsor())
            .setStudyType(trialDto.studyType())
            .setLocation(trialDto.location())
            .setStatus(trialDto.status());
    }


    public TrialDto trialToTrialDto(Trial trial) {
        return new TrialDto(trial.getId(),
            trial.getTitle(),
            trial.getStartDate(),
            trial.getEndDate(),
            trial.getResearcher(),
            trial.getStudyType(),
            trial.getBriefSummary(),
            trial.getDetailedSummary(),
            trial.getSponsor(),
            trial.getCollaborator(),
            trial.getStatus(),
            trial.getLocation(),
            trial.getCrGender(),
            trial.getCrMinAge(),
            trial.getCrMaxAge(),
            trial.getCrFreeText());
    }
}
