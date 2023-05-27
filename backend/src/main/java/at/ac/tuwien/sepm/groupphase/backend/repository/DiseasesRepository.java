package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Disease;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseasesRepository extends JpaRepository<Disease, Long> {
    @Transactional
    @Query("SELECT d FROM Disease d WHERE UPPER(d.name) LIKE UPPER(CONCAT('%',:name,'%'))")
    List<Disease> findDiseasesWithPartOfName(@Param("name") String name);
}
