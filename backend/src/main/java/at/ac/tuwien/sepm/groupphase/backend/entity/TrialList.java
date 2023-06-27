package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "triallist")
public class TrialList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "trial_id")
    private Set<Trial> trial;

    public Long getId() {
        return id;
    }

    public TrialList setId(Long id) {
        this.id = id;
        return this;
    }

    public void addTrial(Trial trial) {
        this.trial.add(trial);
    }

    public ApplicationUser getUser() {
        return user;
    }

    public TrialList setUser(ApplicationUser user) {
        this.user = user;
        return this;
    }

    public Set<Trial> getTrial() {
        return trial;
    }

    public TrialList setTrial(Set<Trial> trial) {
        this.trial = trial;
        return this;
    }

    public String getName() {
        return name;
    }

    public TrialList setName(String name) {
        this.name = name;
        return this;
    }
}
