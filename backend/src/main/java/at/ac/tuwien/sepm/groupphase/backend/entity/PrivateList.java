package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "private_list")
public class PrivateList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "trial_id")
    private Set<Trial> trial;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Set<Trial> getTrial() {
        return trial;
    }

    public void setTrial(Set<Trial> trial) {
        this.trial = trial;
    }
}
