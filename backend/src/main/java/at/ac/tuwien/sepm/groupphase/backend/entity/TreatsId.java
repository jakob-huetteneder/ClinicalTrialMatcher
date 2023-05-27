package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TreatsId implements Serializable {

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

    public TreatsId() {
    }

    public TreatsId(Long patientId, Long doctorId) {
        setPatientId(patientId);
        setDoctorId(doctorId);
    }

    public Long getPatientId() {
        return patientId;
    }

    public TreatsId setPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public TreatsId setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
        TreatsId treatsId = (TreatsId) o;
        return patientId.equals(treatsId.patientId) && doctorId.equals(treatsId.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, doctorId);
    }
}
