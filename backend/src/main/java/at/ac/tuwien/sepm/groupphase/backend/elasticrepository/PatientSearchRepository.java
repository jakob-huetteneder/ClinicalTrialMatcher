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
    void deletePatientById(Long id);
}
