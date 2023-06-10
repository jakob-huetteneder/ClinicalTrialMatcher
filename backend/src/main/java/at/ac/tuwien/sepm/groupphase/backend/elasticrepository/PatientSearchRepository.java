package at.ac.tuwien.sepm.groupphase.backend.elasticrepository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {
    //@Query("{\"bool\": {\"must\": [" +
    //    "{\"terms\": {\"inclusionCriteria\": []}}" +
    //    "], \"must_not\": [" +
    //    "{\"terms\": {\"exclusionCriteria\": []}}" +
    //    "]}}")
    //@Query("{\"bool\": {\"must\": [{\"match\": {\"patient.admissionNote\": ?0}}]}}")
    @Query("{\"match_all\": {}}")
    List<Patient> matchPatientsWithTrial(List<String> inclusion, List<String> exclusion, Pageable pageable);

    //@Query("{\"bool\": {\"must\": [{\"match\": {\"authors.name\": \"?0\"}}]}}")
    //Page<Article> findByAuthorsNameUsingCustomQuery(String name, Pageable pageable);





}
