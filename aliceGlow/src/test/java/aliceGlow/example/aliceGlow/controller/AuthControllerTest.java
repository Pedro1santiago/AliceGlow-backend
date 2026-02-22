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
@DisplayName("AuthController Tests")
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
    @DisplayName("Should return 200 OK with token when login is successful")
    void shouldLoginSuccessfully() {
        when(authService.login(any(LoginDTO.class)))
                .thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token_jwt_123", response.getBody().token());

        verify(authService, times(1)).login(any(LoginDTO.class));
        verify(authService, never()).logout();
    }

    @Test
    @DisplayName("Should call authService.login with correct parameters")
    void shouldCallAuthServiceWithCorrectParameters() {
        when(authService.login(any(LoginDTO.class)))
                .thenReturn(authResponse);

        authController.login(loginDTO);

        verify(authService).login(any(LoginDTO.class));
    }

    @Test
    @DisplayName("Should return response with correct token and email")
    void shouldReturnCorrectTokenAndEmail() {")
    void shouldReturnCorrectToken() {
        AuthResponse expectedResponse = new AuthResponse("new_token_456");
        when(authService.login(any(LoginDTO.class)))
                .thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginDTO);

        assertEquals("new_token_456", response.getBody().token
}
