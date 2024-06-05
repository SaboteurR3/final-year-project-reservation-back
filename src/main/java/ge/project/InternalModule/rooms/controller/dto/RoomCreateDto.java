package ge.project.InternalModule.rooms.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record RoomCreateDto(
        @NotNull
        Long hotelId,
        @NotNull
        String roomNumber,
        @NotNull
        Integer floor,
        @NotNull
        Integer bed,
        @NotNull
        BigDecimal pricePerNight,
        @NotNull
        List<String> imageUrls
) {
}
