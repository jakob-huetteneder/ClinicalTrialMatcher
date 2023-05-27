package at.ac.tuwien.sepm.groupphase.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Treats {

    public enum Status {
        REQUESTED,
        ACCEPTED,
        DECLINED
    }

    @EmbeddedId
    private TreatsId treatsId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("patientId")
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("doctorId")
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    private Status status;


    @JsonBackReference(value = "patient-treats")
    public Patient getPatient() {
        return patient;
    }

    public Treats setPatient(Patient patient) {
        this.patient = patient;
        setTreatsId(null, patient);
        return this;
    }

    @JsonBackReference(value = "doctor-treats")
    public Doctor getDoctor() {
        return doctor;
    }

    public Treats setDoctor(Doctor doctor) {
        this.doctor = doctor;
        setTreatsId(doctor, null);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Treats setStatus(Status status) {
        this.status = status;
        return this;
    }

    private void setTreatsId(Doctor doctor, Patient patient) {
        if (treatsId == null) {
            treatsId = new TreatsId();
        }

        if (doctor != null) {
            treatsId.setDoctorId(doctor.getId());
        }

        if (patient != null) {
            treatsId.setPatientId(patient.getId());
        }
    }
}
