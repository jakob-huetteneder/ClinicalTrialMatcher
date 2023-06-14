package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}
