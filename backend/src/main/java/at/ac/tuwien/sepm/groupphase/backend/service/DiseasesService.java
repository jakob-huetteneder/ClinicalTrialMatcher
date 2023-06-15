package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SearchDto;

import java.util.stream.Stream;

/**
 * Service for managing diseases.
 */
public interface DiseasesService {

    /**
     * Search for diseases.
     *
     * @param searchParams the search parameters
     * @return a stream of diseases
     */
    Stream<DiseaseDto> search(SearchDto searchParams);
}
