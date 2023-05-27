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

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private ApplicationUser applicationUser;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "admission_note", columnDefinition = "TEXT")
    private String admissionNote;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "gender", nullable = false)
    private Gender gender;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<Treats> treats;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<Diagnose> diagnoses;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<Examination> examinations;

    public Long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public LocalDate getBirthdate() {
        return birthdate;
    }


    public Gender getGender() {
        return gender;
    }


    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public Patient setId(Long id) {
        this.id = id;
        return this;
    }

    public Patient setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public Patient setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Patient setLastName(String lastName) {
        this.lastName = lastName;
        return this;
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

    public Patient setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
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
        this.treats = treats;
        return this;
    }

    @JsonManagedReference
    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public Patient setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
        return this;
    }

    @JsonManagedReference
    public Set<Examination> getExaminations() {
        return examinations;
    }

    public Patient setExaminations(Set<Examination> examinations) {
        this.examinations = examinations;
        return this;
    }
}
