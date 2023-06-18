package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.mapstruct.Mapper;

import java.util.List;

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

    List<Trial> trialDtosToTrials(List<TrialDto> trials);

}
