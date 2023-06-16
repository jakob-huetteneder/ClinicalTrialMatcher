package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RegistrationId implements Serializable {

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "trial_id")
    private Long trialId;

    public RegistrationId() {
    }

    public RegistrationId(Long patientId, Long trialId) {
        setPatientId(patientId);
        setTrialId(trialId);
    }

    public Long getPatientId() {
        return patientId;
    }

    public RegistrationId setPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public Long getTrialId() {
        return trialId;
    }

    public RegistrationId setTrialId(Long trialId) {
        this.trialId = trialId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationId registrationId = (RegistrationId) o;
        return patientId.equals(registrationId.patientId) && trialId.equals(registrationId.trialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, trialId);
    }
}
