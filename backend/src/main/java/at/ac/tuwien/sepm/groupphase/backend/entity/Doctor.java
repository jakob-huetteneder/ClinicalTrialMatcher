package at.ac.tuwien.sepm.groupphase.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "doctor")
public class Doctor extends ApplicationUser {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
    private Set<Treats> treats;

    @JsonManagedReference(value = "doctor-treats")
    public Set<Treats> getTreats() {
        return treats;
    }

    public Doctor setTreats(Set<Treats> treats) {
        this.treats = treats;
        return this;
    }
}
