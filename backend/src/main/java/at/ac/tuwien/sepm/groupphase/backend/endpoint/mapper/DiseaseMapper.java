package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
public interface DiseaseMapper {

    DiseaseDto diseaseToDiseaseDto(Disease disease);

    Disease diseaseDtoToDisease(DiseaseDto diseaseDto);
}
