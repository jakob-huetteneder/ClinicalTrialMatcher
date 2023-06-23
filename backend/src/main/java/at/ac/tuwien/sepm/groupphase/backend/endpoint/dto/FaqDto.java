package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Map;

public record FaqDto(
    String message,
    Map<String, String> buttons
) {
}
