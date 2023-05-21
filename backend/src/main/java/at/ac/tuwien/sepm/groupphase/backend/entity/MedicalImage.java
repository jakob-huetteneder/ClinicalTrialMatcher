package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "medical_image")
public class MedicalImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examination_id")
    private Examination examination;

    @Column(name = "image")
    private String image;

    public Examination getExamination() {
        return examination;
    }

    public MedicalImage setExamination(Examination examination) {
        this.examination = examination;
        return this;
    }

    public String getImage() {
        return image;
    }

    public MedicalImage setImage(String image) {
        this.image = image;
        return this;
    }
}

