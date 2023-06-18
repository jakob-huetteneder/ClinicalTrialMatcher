package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrialRegistrationRepository extends JpaRepository<Registration, Long> {

    /**
     * Find registration by patient id and trial id.
     *
     * @param patientId id of the patient
     * @param trialId   id of the trial
     * @return registration
     */
    Optional<Registration> findByRegistrationId_PatientIdAndRegistrationId_TrialId(Long patientId, Long trialId);

    /**
     * Find all registrations by trial id.
     *
     * @param trialId id of the trial
     * @return list of registrations
     */
    List<Registration> findAllByTrial_Id(Long trialId);

    /**
     * Find all registrations by patient id.
     *
     * @param patientId id of the patient
     * @return list of registrations
     */
    List<Registration> findAllByPatient_Id(Long patientId);

}
