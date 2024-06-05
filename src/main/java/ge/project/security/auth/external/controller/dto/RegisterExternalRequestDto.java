package ge.project.security.auth.external.controller.dto;

import ge.project.common.repository.Country;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ge.project.properties.AppConstants;

@Builder
public record RegisterExternalRequestDto(
        @NotEmpty
        String firstname,
        @NotEmpty
        String lastname,
        @NotEmpty
        @Pattern(regexp = AppConstants.EMAIL_PATTERN)
        String email,
        @NotEmpty
        @Pattern(regexp = AppConstants.PHONE_NUMBER_PATTERN)
        String mobileNumber,
        @NotEmpty
        String password,
        @NotNull
        Country country,
        String image
) {
}
