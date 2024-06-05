package ge.project.InternalModule.hotels.controller.dto;

import ge.project.common.repository.City;
import ge.project.properties.AppConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record HotelCreateDto(
        @NotEmpty
        String name,
        @NotEmpty
        String address,
        @NotNull
        City city,
        @NotEmpty
        String zipCode,
        @NotEmpty
        @Pattern(regexp = AppConstants.EMAIL_PATTERN)
        String email,
        @NotEmpty
        @Pattern(regexp = AppConstants.PHONE_NUMBER_PATTERN)
        String phone,
        @NotEmpty
        String description,
        @NotNull
        List<String> imageUrls
) {
}
