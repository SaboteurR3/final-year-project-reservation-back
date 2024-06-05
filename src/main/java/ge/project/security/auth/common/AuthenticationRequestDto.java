package ge.project.security.auth.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import ge.project.properties.AppConstants;

@Builder
public record AuthenticationRequestDto(
        @NotEmpty
        @Pattern(regexp = AppConstants.EMAIL_PATTERN)
        String email,
        @NotEmpty
        String password
) {
}
