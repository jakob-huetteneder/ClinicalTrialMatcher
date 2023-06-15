package at.ac.tuwien.sepm.groupphase.backend.elasticrepository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {

    /**
     * Deletes a patient by its id from the index.
     *
     * @param id the id of the patient to delete
     */
    void deletePatientById(Long id);
}
