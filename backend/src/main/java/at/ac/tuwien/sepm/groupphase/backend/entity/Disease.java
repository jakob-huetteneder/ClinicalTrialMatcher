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
import org.hibernate.annotations.OnDelete;

@Entity
@Table(name = "disease")
public class Disease {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "link")
    private String link;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trial_id")
    @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Trial trial;

    public String getName() {
        return name;
    }

    public Disease setName(String name) {
        this.name = name;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Disease setLink(String link) {
        this.link = link;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Disease setId(Long id) {
        this.id = id;
        return this;
    }

    public Trial getTrial() {
        return trial;
    }

    public Disease setTrial(Trial trial) {
        this.trial = trial;
        return this;
    }

}
