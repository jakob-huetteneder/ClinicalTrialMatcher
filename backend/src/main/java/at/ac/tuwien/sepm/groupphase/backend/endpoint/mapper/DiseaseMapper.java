package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import org.mapstruct.Mapper;

/**
 * Mapper for mapping {@link Disease} to {@link DiseaseDto} and vice versa.
 */
@Mapper
public interface DiseaseMapper {

    /**
     * Maps the given disease to a diseaseDto.
     *
     * @param disease to be mapped
     * @return the mapped diseaseDto
     */
    DiseaseDto diseaseToDiseaseDto(Disease disease);

    /**
     * Maps the given diseaseDto to a disease.
     *
     * @param diseaseDto to be mapped
     * @return the mapped disease
     */
    Disease diseaseDtoToDisease(DiseaseDto diseaseDto);
}
