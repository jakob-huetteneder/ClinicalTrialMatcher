package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByApplicationUser_Id(Long userId);

    @Query("SELECT p FROM Patient p WHERE lower(concat(p.firstName, p.lastName)) LIKE concat('%', lower(:search), '%')")
    List<Patient> findAllContaining(@Param("search") String search);
}
