package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import java.util.List;
import java.util.Set;

import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import org.mapstruct.Mapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;

@Mapper
public interface TreatsMapper {

    List<TreatsDto> treatsToTreatsDto(List<Treats> treats);

    List<TreatsDto> treatsToTreatsDto(Set<Treats> treats);

    TreatsDto treatsToTreatsDto(Treats treats);

    Treats treatsDtoToTreats(TreatsDto treatsDto);
}