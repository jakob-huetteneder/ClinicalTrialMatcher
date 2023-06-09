package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrialRegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByRegistrationId_PatientIdAndRegistrationId_TrialId(Long patientId, Long trialId);

    List<Registration> findAllByTrial_Id(Long trialId);
}
