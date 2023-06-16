package at.ac.tuwien.sepm.groupphase.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDate;

@Entity
@Table(name = "registration")
public class Registration {

    public enum Status {
        PROPOSED, // doctor proposes patient to trial
        PATIENT_ACCEPTED, // patient accepted trial
        ACCEPTED,
        DECLINED
    }

    @EmbeddedId
    private RegistrationId registrationId;


    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("patientId")
    @JoinColumn(name = "patient_id")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("trialId")
    @JoinColumn(name = "trial_id")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Trial trial;

    @Column(name = "status")
    private Status status;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public Registration setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @JsonBackReference(value = "patient-registration")
    public Patient getPatient() {
        return patient;
    }

    public Registration setPatient(Patient patient) {
        this.patient = patient;
        setRegistrationId(null, patient);
        return this;
    }

    @JsonBackReference(value = "trial-registration")
    public Trial getTrial() {
        return trial;
    }

    public Registration setTrial(Trial trial) {
        this.trial = trial;
        setRegistrationId(trial, null);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Registration setStatus(Status status) {
        this.status = status;
        return this;
    }

    private void setRegistrationId(Trial trial, Patient patient) {
        if (registrationId == null) {
            registrationId = new RegistrationId();
        }

        if (trial != null) {
            registrationId.setTrialId(trial.getId());
        }

        if (patient != null) {
            registrationId.setPatientId(patient.getId());
        }
    }
}
