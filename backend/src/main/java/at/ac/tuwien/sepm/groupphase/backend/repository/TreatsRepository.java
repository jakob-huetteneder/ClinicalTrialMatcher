package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.TreatsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

/**
 * Repository for treats relationships
 */
public interface TreatsRepository extends JpaRepository<Treats, TreatsId> {

    /**
     * Find treats relationship between patient and doctor
     *
     * @param patientId id of the patient
     * @param doctorId  id of the doctor
     * @return treats relationship
     */
    Optional<Treats> findByTreatsId_PatientIdAndTreatsId_DoctorId(Long patientId, Long doctorId);

    /**
     * Find all treats relationships for a doctor.
     *
     * @param doctorId id of the doctor
     * @param search   search string for patient name
     * @return set of treats relationships
     */
    @Query("SELECT t FROM Treats t WHERE t.treatsId.doctorId = :doctorId"
        + " AND (t.treatsId.patientId IN (SELECT p.id FROM Patient p WHERE lower(concat(p.firstName, ' ', p.lastName)) LIKE concat('%', lower(:search), '%')))")
    Set<Treats> findAllByDoctorId(@Param("doctorId") Long doctorId, @Param("search") String search);

    /**
     * Find all treats relationships for a patient.
     *
     * @param patientUserId id of the patients user account
     * @param search        search string for doctor name
     * @return set of treats relationships
     */
    @Query("SELECT t FROM Treats t WHERE t.treatsId.patientId = (SELECT p.id FROM Patient p WHERE p.applicationUser.id = :userId) "
        + "AND (t.treatsId.doctorId IN (SELECT d.id FROM Doctor d WHERE lower(concat(d.firstName, ' ', d.lastName)) LIKE concat('%', lower(:search), '%')))")
    Set<Treats> findAllByPatientId(@Param("userId") Long patientUserId, @Param("search") String search);
}
