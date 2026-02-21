package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}