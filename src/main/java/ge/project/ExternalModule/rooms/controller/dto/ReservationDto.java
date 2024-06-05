package ge.project.ExternalModule.rooms.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDto(
        @NotNull
        LocalDate reservedFrom,
        @NotNull
        LocalDate reservedTo
) {
}
