package ge.project.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static ge.project.security.user.repository.entity.Role.INTERNAL_USER;
import static ge.project.security.user.repository.entity.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf()
                .disable()
                .cors(corsConfig ->
                        corsConfig.configurationSource(corsConfigurationSource()))
                .exceptionHandling(config ->
                        config.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(
                        configurer -> configurer
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/swagger/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/profile").hasAnyRole(USER.name(), INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/internal/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/external/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/external/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/internal/hotels/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.POST, "/internal/hotels/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PUT, "/internal/hotels/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/internal/hotels/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PATCH, "/internal/hotels/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.GET, "/internal/rooms", "/internal/rooms/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.POST, "/internal/rooms/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PUT, "/internal/rooms/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.DELETE,"/internal/rooms/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PATCH,"/internal/rooms/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.GET,"/external/rooms/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.PATCH,"/external/rooms/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/external/hotels/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.POST, "/external/hotels/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/internal/tours/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.POST, "/internal/tours/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PUT, "/internal/tours/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.PATCH, "/internal/tours/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/internal/tours/**").hasAnyRole(INTERNAL_USER.name())
                                .requestMatchers(HttpMethod.GET, "/external/tours/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.PATCH, "/external/tours/**").hasAnyRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/recommended-hotels").hasAnyRole(USER.name())

                )
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                .and().build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("origin", "content-type", "accept", "authorization", "enctype", "content-disposition"));
        configuration.setMaxAge((long) (10 * 60));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
