package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Trial;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.time.LocalDate;

public record FilterDto(
    Gender gender,
    Trial.Status recruiting,
    LocalDate startDate,
    LocalDate endDate,
    Integer age,
    Integer page,
    Integer size
) {
}
