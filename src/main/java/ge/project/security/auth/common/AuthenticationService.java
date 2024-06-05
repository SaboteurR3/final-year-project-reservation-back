package ge.project.security.auth.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.project.common.repository.Country;
import ge.project.exception.BusinessException;
import ge.project.security.auth.external.controller.dto.RegisterExternalRequestDto;
import ge.project.security.auth.internal.controller.dto.RegisterInternalRequestDto;
import ge.project.security.token.model.Token;
import ge.project.security.token.model.TokenType;
import ge.project.security.token.repository.TokenRepository;
import ge.project.security.user.repository.UserRepository;
import ge.project.security.user.repository.entity.Role;
import ge.project.security.user.repository.entity.User;
import ge.project.security.user.repository.entity.UserStatus;
import ge.project.security.user.repository.entity.UserType;
import ge.project.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerExternalUser(RegisterExternalRequestDto request) {
        checkIfEmailOrPhoneExists(request.email(), request.mobileNumber());
        var user = buildExternalUser(request);
        return saveUserAndGenerateResponse(user);
    }

    public AuthenticationResponse registerInternalUser(RegisterInternalRequestDto request) {
        checkIfEmailOrPhoneExists(request.email(), request.mobileNumber());
        var user = buildInternalUser(request);
        return saveUserAndGenerateResponse(user);
    }

    public void checkIfEmailOrPhoneExists(String email, String mobileNumber) {
        Optional<User> byEmail = repository.findByEmail(email);
        Optional<User> byPhone = repository.findByMobileNumber(mobileNumber);
        if (byEmail.isPresent() || byPhone.isPresent()) {
            throw new BusinessException("email_or_phone_number_exists");
        }
    }

    private User buildExternalUser(RegisterExternalRequestDto request) {
        return User.builder()
                .firstName(request.firstname())
                .lastName(request.lastname())
                .userType(UserType.EXTERNAL)
                .status(UserStatus.ACTIVE)
                .email(request.email())
                .registrationDate(LocalDateTime.now())
                .mobileNumber(request.mobileNumber())
                .image(request.image())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .country(request.country())
                .build();
    }

    private User buildInternalUser(RegisterInternalRequestDto request) {
        return User.builder()
                .firstName(request.firstname())
                .lastName(request.lastname())
                .userType(UserType.INTERNAL)
                .status(UserStatus.ACTIVE)
                .email(request.email())
                .registrationDate(LocalDateTime.now())
                .mobileNumber(request.mobileNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.INTERNAL_USER)
                .country(Country.GEORGIA)
                .build();
    }

    private AuthenticationResponse saveUserAndGenerateResponse(User user) {
        repository.saveAndFlush(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = repository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
