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
import org.hibernate.annotations.OnDelete;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;

@Entity
@Table(name = "examination")
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "examination")
    private MedicalImage medicalImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Patient patient;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd")
    private LocalDate date;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;

    @JsonBackReference

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

    public Examination setId(Long id) {
        this.id = id;
        return this;
    }

    public MedicalImage getMedicalImage() {
        return medicalImage;
    }

    public Examination setMedicalImage(MedicalImage medicalImage) {
        this.medicalImage = medicalImage;
        return this;
    }

    public Long getId() {
        return this.id;
    }
}
