package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DiseaseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;

import java.util.stream.Stream;

public interface DiseasesService {

    /**
     * Search for diseases.
     *
     * @param searchParams the search parameters
     * @return the stream of diseases
     */
    Stream<DiseaseDto> search(SearchDto searchParams);

    /**
     * Set Wikipedia Links for All Diseases of the Disease Table.
     * Automatically executes on a daily basis.
     */
    void setAllDiseaseLinks();

    /**
     * Set the Wikipedia Link for a Disease.
     *
     * @param disease the disease
     */
    void setDiseaseLink(Disease disease);
}
