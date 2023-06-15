package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Diagnose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for diagnoses
 */
@Repository
public interface DiagnosesRepository extends JpaRepository<Diagnose, Long> {

    /**
     * Delete all diagnoses by patient id
     *
     * @param patientId of the patient
     */
    void deleteAllByPatient_Id(Long patientId);
}
