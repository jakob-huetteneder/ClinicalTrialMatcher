package at.ac.tuwien.sepm.groupphase.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "diagnose")
public class Diagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disease_id")
    private Disease disease;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "note")
    private String note;

    @JsonBackReference
    public Patient getPatient() {
        return patient;
    }

    public Diagnose setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public Disease getDisease() {
        return disease;
    }

    public Diagnose setDisease(Disease disease) {
        this.disease = disease;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Diagnose setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Diagnose setNote(String note) {
        this.note = note;
        return this;
    }
}
