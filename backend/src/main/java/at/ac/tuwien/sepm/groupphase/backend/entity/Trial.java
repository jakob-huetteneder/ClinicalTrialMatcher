package at.ac.tuwien.sepm.groupphase.backend.entity;


import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
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

    @Column(name = "title")
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "researcher_id", nullable = false)
    private Researcher researcher;

    @Column(name = "study_type")
    private String studyType;

    @Column(name = "brief_summary")
    private String briefSummary;

    @Column(name = "detailed_summary")
    private String detailedSummary;

    @Column(name = "sponsor")
    private String sponsor;

    @Column(name = "collaborator")
    private String collaborator;

    @Column(name = "status")
    private String status;

    @Column(name = "location")
    private String location;

    @Column(name = "cr_gender")
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

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }

    public String getDetailedSummary() {
        return detailedSummary;
    }

    public void setDetailedSummary(String detailedSummary) {
        this.detailedSummary = detailedSummary;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(String collaborator) {
        this.collaborator = collaborator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Gender getCrGender() {
        return crGender;
    }

    public void setCrGender(Gender crGender) {
        this.crGender = crGender;
    }

    public int getCrMinAge() {
        return crMinAge;
    }

    public void setCrMinAge(int crMinAge) {
        this.crMinAge = crMinAge;
    }

    public int getCrMaxAge() {
        return crMaxAge;
    }

    public void setCrMaxAge(int crMaxAge) {
        this.crMaxAge = crMaxAge;
    }

    public String getCrFreeText() {
        return crFreeText;
    }

    public void setCrFreeText(String crFreeText) {
        this.crFreeText = crFreeText;
    }
}
