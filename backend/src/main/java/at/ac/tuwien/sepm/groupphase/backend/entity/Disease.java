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

    @Column(name = "synonyms")
    private String synonyms;

    public String getName() {
        return name;
    }

    public Disease setName(String name) {
        this.name = name;
        return this;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public Disease setSynonyms(String synonyms) {
        this.synonyms = synonyms;
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
