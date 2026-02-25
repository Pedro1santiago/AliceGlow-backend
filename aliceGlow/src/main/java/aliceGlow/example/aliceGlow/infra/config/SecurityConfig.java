package aliceGlow.example.aliceGlow.infra.config;

import aliceGlow.example.aliceGlow.infra.security.JwtAuthenticationFilter;
import aliceGlow.example.aliceGlow.infra.filter.CorrelationIdFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final CorrelationIdFilter correlationIdFilter;
    private final Environment environment;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            JwtAuthenticationFilter jwtFilter,
            CorrelationIdFilter correlationIdFilter,
            Environment environment,
            ObjectMapper objectMapper
    ) {
        this.jwtFilter = jwtFilter;
        this.correlationIdFilter = correlationIdFilter;
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(getAllowedOrigins());
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    .requestMatchers("/error").permitAll()

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui.html",
                            "/swagger-ui/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/products/**")
                        .hasAnyRole("USER","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/products/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/products/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/sales/**")
                        .hasAnyRole("USER","ADMIN")

                        .requestMatchers(HttpMethod.POST, "/sales/**")
                        .hasAnyRole("USER","ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/sales/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/users/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/reports/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/cash-boxes/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/stock-purchases/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                    .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            writeUnauthorizedResponse(request.getRequestURI(), response);
                        })
                    )

                .addFilterBefore(correlationIdFilter, JwtAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private List<String> getAllowedOrigins() {
        String allowedOriginsValue = environment.getProperty("app.cors.allowed-origins", "");
        return Arrays.stream(allowedOriginsValue.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toList());
    }

    private void writeUnauthorizedResponse(String path, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String correlationId = MDC.get("correlationId");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Unauthorized");
        body.put("path", path);
        if (correlationId != null && !correlationId.isBlank()) {
            body.put("correlationId", correlationId);
        }

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}