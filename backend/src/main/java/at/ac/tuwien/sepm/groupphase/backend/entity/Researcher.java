package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "researcher")
public class Researcher extends ApplicationUser {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "trial_id")
    private Set<Trial> trials;
}
