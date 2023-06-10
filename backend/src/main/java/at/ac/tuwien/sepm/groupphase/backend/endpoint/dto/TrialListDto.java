package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;

import java.util.List;

public class TrialListDto {

    private Long id;
    private String name;
    private ApplicationUser user;
    private List<Trial> trial;

    public Long getId() {
        return id;
    }

    public TrialListDto setId(Long id) {
        this.id = id;
        return this;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public List<Trial> getTrial() {
        return trial;
    }

    public void setTrial(List<Trial> trial) {
        this.trial = trial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
