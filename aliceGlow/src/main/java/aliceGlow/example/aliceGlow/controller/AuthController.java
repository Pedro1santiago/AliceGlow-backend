package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.auth.AuthResponse;
import aliceGlow.example.aliceGlow.dto.auth.LoginDTO;
import aliceGlow.example.aliceGlow.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Performs login and returns a JWT token for authenticating other routes.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request){
        return ResponseEntity.ok(authService.login(request));
    }
}