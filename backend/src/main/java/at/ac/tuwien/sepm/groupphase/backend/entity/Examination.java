package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "examination")
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;

    public Patient getPatient() {
        return patient;
    }

    public Examination setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public String getName() {
        return name;
    }

    public Examination setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Examination setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getType() {
        return type;
    }

    public Examination setType(String type) {
        this.type = type;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Examination setNote(String note) {
        this.note = note;
        return this;
    }
}
