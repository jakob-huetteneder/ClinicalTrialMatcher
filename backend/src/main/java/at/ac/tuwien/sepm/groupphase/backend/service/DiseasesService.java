package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SearchDto;

import java.util.stream.Stream;

public interface DiseasesService {

    Stream<DiseaseDto> search(SearchDto searchParams);
}
