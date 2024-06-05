package ge.project.InternalModule.tours.controller.dto;

import ge.project.InternalModule.tours.repository.entity.TourImage;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record TourDetailsGetDto(
        String name,
        String description,
        BigDecimal price,
        LocalDate startDate,
        LocalDate endDate,
        Integer totalSeats,
        Integer reservedSeats,
        Boolean isActive,
        List<String> imageUrls
) {
}
