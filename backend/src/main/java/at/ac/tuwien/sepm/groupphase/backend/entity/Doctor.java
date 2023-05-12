package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "doctor")
public class Doctor extends ApplicationUser {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Set<Patient> patients;


}
