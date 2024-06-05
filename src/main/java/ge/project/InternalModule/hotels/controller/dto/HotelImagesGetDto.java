package ge.project.InternalModule.hotels.controller.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record HotelImagesGetDto(
        List<String> imageUrls
) {
}
