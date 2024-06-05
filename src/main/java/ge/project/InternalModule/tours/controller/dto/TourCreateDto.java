package ge.project.InternalModule.tours.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record TourCreateDto(
        @NotEmpty
        String name,
        @NotEmpty
        String description,
        @NotNull
        BigDecimal price,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        Integer totalSeats,
        @NotNull
        List<String> imageUrls
) {
}
