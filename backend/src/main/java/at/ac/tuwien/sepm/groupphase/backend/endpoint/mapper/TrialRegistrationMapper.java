package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TrialRegistrationMapper {

    List<TrialRegistrationDto> trialRegistrationToTrialRegistrationDto(List<Registration> registrations);

    TrialRegistrationDto trialRegistrationToTrialRegistrationDto(Registration registration);

    Registration trialRegistrationDtoToTrialRegistration(TrialRegistrationDto trialRegistrationDto);
}
