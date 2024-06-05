package ge.project.security.auth.external.controller;

import ge.project.security.auth.common.AuthenticationRequestDto;
import ge.project.security.auth.common.AuthenticationResponse;
import ge.project.security.auth.common.AuthenticationService;
import ge.project.security.auth.external.controller.dto.RegisterExternalRequestDto;
import ge.project.common.repository.Country;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/external/auth")
@RequiredArgsConstructor
public class ExternalAuthenticationController {

    private final AuthenticationService service;

    @GetMapping("countries")
    public Country[] getCountries() {
        return Country.values();
    }

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @NotNull @Valid RegisterExternalRequestDto request
    ) {
        return ResponseEntity.ok(service.registerExternalUser(request));
    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @NotNull @Valid AuthenticationRequestDto request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}