package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.exception;

import java.util.List;

public record ValidationExceptionDto(

    String message,
    List<String> errors
) {
}
