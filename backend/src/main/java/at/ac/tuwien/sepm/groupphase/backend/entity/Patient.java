package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patient")
@Document(indexName = "patients")
@Setting(settingPath = "es-settings.json")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private ApplicationUser applicationUser;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    //@Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "admission_note", columnDefinition = "TEXT")
    private String admissionNote;

    @Column(name = "birthdate", nullable = false)
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd")
    private LocalDate birthdate;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "verification")
    private String verification;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient", orphanRemoval = true)
    @Field(type = FieldType.Nested)
    private Set<Treats> treats = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient", orphanRemoval = true)
    @Field(type = FieldType.Nested)
    private Set<Diagnose> diagnoses = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient", orphanRemoval = true)
    @Field(type = FieldType.Nested)
    private Set<Examination> examinations = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Patient setId(Long id) {
        this.id = id;
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public Patient setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Patient setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Patient setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Patient setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAdmissionNote() {
        return admissionNote;
    }

    public Patient setAdmissionNote(String admissionNote) {
        this.admissionNote = admissionNote;
        return this;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Patient setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Patient setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    @JsonManagedReference(value = "patient-treats")
    public Set<Treats> getTreats() {
        return treats;
    }

    public Patient setTreats(Set<Treats> treats) {
        this.treats.clear();
        if (treats != null) {
            this.treats.addAll(treats);
        }
        return this;
    }

    @JsonManagedReference
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public Patient setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses.clear();
        if (diagnoses != null) {
            this.diagnoses.addAll(diagnoses);
        }
        return this;
    }

    @JsonManagedReference
    public Set<Examination> getExaminations() {
        return examinations;
    }

    public Patient setExaminations(Set<Examination> examinations) {
        this.examinations.clear();
        if (examinations != null) {
            this.examinations.addAll(examinations);
        }
        return this;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
