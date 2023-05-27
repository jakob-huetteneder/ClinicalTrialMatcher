package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import at.ac.tuwien.sepm.groupphase.backend.entity.TreatsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TreatsRepository extends JpaRepository<Treats, TreatsId> {

    Optional<Treats> findByTreatsId_PatientIdAndTreatsId_DoctorId(Long patientId, Long doctorId);

    @Query("SELECT t FROM Treats t WHERE t.treatsId.doctorId = :doctorId")
    Set<Treats> findAllByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT t FROM Treats t WHERE t.treatsId.patientId = (SELECT p.id FROM Patient p WHERE p.applicationUser.id = :userId)")
    Set<Treats> findAllByPatientId(@Param("userId") Long patientUserId);
}
