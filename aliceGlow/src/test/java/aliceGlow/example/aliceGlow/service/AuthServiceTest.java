package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.dto.auth.AuthResponse;
import aliceGlow.example.aliceGlow.dto.auth.LoginDTO;
import aliceGlow.example.aliceGlow.infra.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private LoginDTO loginDTO;
    private UserDetails userDetails;
    private String token;

    @BeforeEach
    void setup() {
        loginDTO = new LoginDTO("test@email.com", "123456");
        userDetails = new User("test@email.com", "123456", Collections.emptyList());
        token = "jwt_token_example";
    }

    @Test
    void shouldAuthenticateAndReturnToken() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtService.generateToken(userDetails))
                .thenReturn(token);

        AuthResponse response = authService.login(loginDTO);

        assertNotNull(response);
        assertEquals(token, response.token());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void shouldCallAuthenticationManagerWithCredentials() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtService.generateToken(userDetails))
                .thenReturn(token);

        authService.login(loginDTO);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldGenerateTokenFromAuthenticatedUser() {
        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtService.generateToken(userDetails))
                .thenReturn(token);

        AuthResponse response = authService.login(loginDTO);

        assertEquals(token, response.token());
        verify(jwtService).generateToken(userDetails);
    }
}