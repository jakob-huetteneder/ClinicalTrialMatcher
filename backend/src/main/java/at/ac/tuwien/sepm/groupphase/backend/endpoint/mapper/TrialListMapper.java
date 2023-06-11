package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TrialListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.TrialList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrialListMapper {

    private final TrialMapper trialMapper;

    public TrialListMapper(TrialMapper trialMapper) {
        this.trialMapper = trialMapper;
    }

    public TrialListDto trialListToTrialListDto(TrialList triallist) {
        TrialListDto trialListDto = new TrialListDto();
        trialListDto.setId(triallist.getId());
        trialListDto.setName(triallist.getName());
        trialListDto.setUser(triallist.getUser());
        trialListDto.setTrial(trialMapper.trialToTrialDto(triallist.getTrial()));
        return trialListDto;
    }

    public TrialList trialListDtoToTrialList(TrialListDto trialListDto) {
        TrialList trialList = new TrialList();
        trialList.setId(trialListDto.getId());
        trialList.setName(trialListDto.getName());
        trialList.setUser(trialListDto.getUser());
        trialList.setTrial(trialMapper.trialDtosToTrials(trialListDto.getTrial()));
        return trialList;
    }

    public List<TrialList> trialListDtosToTrialLists(List<TrialListDto> trialListDtos) {
        List<TrialList> trialLists = null;
        for (TrialListDto trialListDto : trialListDtos) {
            trialLists.add(trialListDtoToTrialList(trialListDto));
        }
        return trialLists;
    }

    public List<TrialListDto> trialListsToTrialListDtos(List<TrialList> trialLists) {
        List<TrialListDto> trialListDtos = new ArrayList<>();
        if (trialLists == null) {
            return null;
        }
        for (TrialList trialList : trialLists) {
            if (trialList != null) {
                trialListDtos.add(trialListToTrialListDto(trialList));
            }
        }
        return trialListDtos;
    }
}
