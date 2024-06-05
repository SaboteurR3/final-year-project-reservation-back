package ge.project.security.auth.internal.controller.dto;

import ge.project.properties.AppConstants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegisterInternalRequestDto(
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
        String password
) {
}
