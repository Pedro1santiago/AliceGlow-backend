package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.auth.AuthResponse;
import aliceGlow.example.aliceGlow.dto.auth.LoginDTO;
import aliceGlow.example.aliceGlow.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginDTO loginDTO;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("test@example.com", "password123");
        authResponse = new AuthResponse("token_jwt_123");
    }

    @Test
    void shouldLoginSuccessfully() {
        when(authService.login(any())).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token_jwt_123", response.getBody().token());
        verify(authService).login(any());
    }
}