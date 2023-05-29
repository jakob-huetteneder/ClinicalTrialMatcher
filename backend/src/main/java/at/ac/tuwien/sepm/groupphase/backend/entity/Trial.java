package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.TrialStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "trial")
public class Trial {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "researcher_id", nullable = false)
    private Researcher researcher;

    @Column(name = "study_type", nullable = false)
    private String studyType;

    @Column(name = "brief_summary", nullable = false)
    private String briefSummary;

    @Column(name = "detailed_summary", nullable = false)
    private String detailedSummary;

    @Column(name = "sponsor", nullable = false)
    private String sponsor;

    @Column(name = "collaborator", nullable = false)
    private String collaborator;

    @Column(name = "status", nullable = false)
    private TrialStatus status;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "cr_gender", nullable = false)
    private Gender crGender;

    @Column(name = "cr_min_age")
    private int crMinAge;

    @Column(name = "cr_max_age")
    private int crMaxAge;

    @Column(name = "cr_free_text")
    private String crFreeText;

    public String getTitle() {
        return title;
    }



    public LocalDate getStartDate() {
        return startDate;
    }


    public LocalDate getEndDate() {
        return endDate;
    }



    public Researcher getResearcher() {
        return researcher;
    }



    public String getStudyType() {
        return studyType;
    }



    public String getBriefSummary() {
        return briefSummary;
    }



    public String getDetailedSummary() {
        return detailedSummary;
    }



    public String getSponsor() {
        return sponsor;
    }



    public String getCollaborator() {
        return collaborator;
    }


    public TrialStatus getStatus() {
        return status;
    }



    public String getLocation() {
        return location;
    }



    public Gender getCrGender() {
        return crGender;
    }



    public int getCrMinAge() {
        return crMinAge;
    }



    public int getCrMaxAge() {
        return crMaxAge;
    }



    public String getCrFreeText() {
        return crFreeText;
    }



    public Long getId() {
        return id;
    }

    public Trial setId(Long id) {
        this.id = id;
        return this;
    }

    public Trial setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public Trial setResearcher(Researcher researcher) {
        this.researcher = researcher;
        return this;
    }

    public Trial setStudyType(String studyType) {
        this.studyType = studyType;
        return this;
    }

    public Trial setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
        return this;
    }

    public Trial setDetailedSummary(String detailedSummary) {
        this.detailedSummary = detailedSummary;
        return this;
    }

    public Trial setSponsor(String sponsor) {
        this.sponsor = sponsor;
        return this;
    }

    public Trial setCollaborator(String collaborator) {
        this.collaborator = collaborator;
        return this;
    }

    public Trial setStatus(TrialStatus status) {
        this.status = status;
        return this;
    }

    public Trial setLocation(String location) {
        this.location = location;
        return this;
    }

    public Trial setCrGender(Gender crGender) {
        this.crGender = crGender;
        return this;
    }

    public Trial setCrMinAge(int crMinAge) {
        this.crMinAge = crMinAge;
        return this;
    }

    public Trial setCrMaxAge(int crMaxAge) {
        this.crMaxAge = crMaxAge;
        return this;
    }

    public Trial setCrFreeText(String crFreeText) {
        this.crFreeText = crFreeText;
        return this;
    }

    public Trial setTitle(String title) {
        this.title = title;
        return this;
    }

    public Trial setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }
}
