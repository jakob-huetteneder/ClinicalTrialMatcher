package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import org.springframework.stereotype.Component;

@Component
public class DiseaseMapper {

    public DiseaseDto diseaseToDiseaseDto(Disease disease) {
        return new DiseaseDto(disease.getId(), disease.getName(), disease.getSynonyms());
    }
}
