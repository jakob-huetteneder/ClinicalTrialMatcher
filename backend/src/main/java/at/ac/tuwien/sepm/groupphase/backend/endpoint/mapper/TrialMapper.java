package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TrialMapper {

    List<TrialDto> trialToTrialDto(List<Trial> trials);

    TrialDto trialToTrialDto(Trial trial);

    Trial trialDtoToTrial(TrialDto trialDto);
}
