package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class TrialListDto {

    private Long id;
    @NotBlank(message = "Name must not be blank")
    private String name;
    private ApplicationUser user;
    private List<TrialDto> trial;

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

    public List<TrialDto> getTrial() {
        return trial;
    }

    public void setTrial(List<TrialDto> trial) {
        this.trial = trial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
