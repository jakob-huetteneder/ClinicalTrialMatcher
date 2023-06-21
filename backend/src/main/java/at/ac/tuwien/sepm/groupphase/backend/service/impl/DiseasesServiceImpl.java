package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseaseRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import io.github.fastily.jwiki.core.Wiki;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DiseasesServiceImpl implements DiseasesService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;

    public DiseasesServiceImpl(DiseaseRepository diseaseRepository, DiseaseMapper diseaseMapper) {
        this.diseaseRepository = diseaseRepository;
        this.diseaseMapper = diseaseMapper;
    }

    @Override
    public Stream<DiseaseDto> search(SearchDto searchParams) {
        LOG.trace("search({})", searchParams);
        return diseaseRepository.findDiseasesWithPartOfName(searchParams.name()).stream()
            .limit(searchParams.limit()).map(diseaseMapper::diseaseToDiseaseDto);
    }

    @Override
    public DiseaseDto getPersistedDiseaseWithLink(Disease disease) {
        LOG.trace("getPersistedDisease({})", disease);

        List<Disease> diseases = diseaseRepository.findDiseasesByName(disease.getName());
        if (diseases.isEmpty()) {
            Disease persistedDisease = diseaseRepository.save(disease);
            setDiseaseLink(disease);
            return diseaseMapper.diseaseToDiseaseDto(persistedDisease);
        } else {
            return diseaseMapper.diseaseToDiseaseDto(diseases.get(0));
        }
    }

    @Override
    @Scheduled(fixedRate = 8640 * 1000) // 24 hours = 8640 seconds, fixedRate uses ms
    public void setAllDiseaseLinks() {
        LOG.debug("Setting Wikipedia Links for all Diseases");
        diseaseRepository.findAll().forEach(this::setDiseaseLink);
    }

    @Override
    public void setDiseaseLink(Disease disease) {
        LOG.debug("Setting Wikipedia Link for Disease {}", disease.getName());
        Wiki wiki = new Wiki.Builder().build();
        if (wiki.exists(disease.getName())) {
            String diseaseName = disease.getName();
            String[] words = diseaseName.split(" ");
            String query = String.join("_", words);
            // query: e.g. "Diabetes mellitus" -> "Diabetes_mellitus"
            disease.setLink("https://en.wikipedia.org/wiki/" + query);
            diseaseRepository.save(disease);
        }
    }
}
