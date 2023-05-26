package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PatientDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.DiseaseMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PatientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import at.ac.tuwien.sepm.groupphase.backend.repository.DiseasesRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PatientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DiseasesService;
import at.ac.tuwien.sepm.groupphase.backend.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DiseasesServiceImpl implements DiseasesService {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final DiseasesRepository diseasesRepository;
    private final DiseaseMapper diseaseMapper;

    public DiseasesServiceImpl(DiseasesRepository diseasesRepository, DiseaseMapper diseaseMapper) {
        this.diseasesRepository = diseasesRepository;
        this.diseaseMapper = diseaseMapper;
    }

    @Override
    public Stream<DiseaseDto> search(DiseaseSearchDto searchParams) {
        return diseasesRepository.findDiseasesWithPartOfName(searchParams.name()).stream()
            .limit(searchParams.limit()).map(diseaseMapper::diseaseToDiseaseDto);
    }
}
