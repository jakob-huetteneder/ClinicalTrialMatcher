package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for examinations.
 */
@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {

    /**
     * Find all examinations with matching name.
     *
     * @param name required to be part of the examination name
     * @return list of examinations with matching name
     */
    @Query("SELECT e FROM Examination e WHERE e.name LIKE CONCAT('%',:name,'%')")
    List<Disease> findExaminationsWithPartOfName(@Param("name") String name);

    /**
     * Delete all examinations of a patient.
     *
     * @param patientId of the patient
     */
    void deleteAllByPatient_Id(Long patientId);
}
