package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TreatsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Treats;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

/**
 * Mapper for mapping {@link Treats} to {@link TreatsDto} and vice versa.
 */
@Mapper
public interface TreatsMapper {

    /**
     * Maps the given list of treats to a list of treatsDto.
     *
     * @param treats to be mapped
     * @return the mapped treatsDto
     */
    List<TreatsDto> treatsToTreatsDto(List<Treats> treats);

    /**
     * Maps the given set of treats to a list of treatsDto.
     *
     * @param treats to be mapped
     * @return the mapped treatsDto
     */
    List<TreatsDto> treatsToTreatsDto(Set<Treats> treats);

    /**
     * Maps the given treats to a treatsDto.
     *
     * @param treats to be mapped
     * @return the mapped treats
     */
    TreatsDto treatsToTreatsDto(Treats treats);

    /**
     * Maps the given treatsDto to a treats.
     *
     * @param treatsDto to be mapped
     * @return the mapped treats
     */
    Treats treatsDtoToTreats(TreatsDto treatsDto);
}