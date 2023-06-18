package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for diseases.
 */
@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    /**
     * Find all diseases with matching name.
     *
     * @param name required to be part of the disease name
     * @return list of diseases with matching name
     */
    @Query("SELECT d FROM Disease d WHERE UPPER(d.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    List<Disease> findDiseasesWithPartOfName(@Param("name") String name);

    /**
     * Find all diseases with exact name.
     *
     * @param name of the disease
     * @return list of diseases with exact name
     */
    List<Disease> findDiseasesByName(String name);
}
