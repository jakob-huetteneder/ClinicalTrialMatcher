package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Mapper for mapping {@link Trial} to {@link TrialDto} and vice versa.
 */
@Mapper
public interface TrialMapper {

    /**
     * Maps the given list of trials to a list of trialDtos.
     *
     * @param trials to be mapped
     * @return the mapped trialDtos
     */
    List<TrialDto> trialToTrialDto(List<Trial> trials);

    /**
     * Maps the given set of trials to a set of trialDtos.
     *
     * @param trials to be mapped
     * @return the mapped trialDtos
     */
    Set<TrialDto> trialToTrialDto(Set<Trial> trials);

    /**
     * Maps the given trial to a trialDto.
     *
     * @param trial to be mapped
     * @return the mapped trialDto
     */
    TrialDto trialToTrialDto(Trial trial);

    /**
     * Maps the given trialDto to a trial.
     *
     * @param trialDto to be mapped
     * @return the mapped trial
     */
    Trial trialDtoToTrial(TrialDto trialDto);

    /**
     * Maps the given list of trialDtos to a list of trials.
     *
     * @param trials to be mapped
     * @return the mapped trials
     */
    Set<Trial> trialDtosToTrials(Set<TrialDto> trials);

}
