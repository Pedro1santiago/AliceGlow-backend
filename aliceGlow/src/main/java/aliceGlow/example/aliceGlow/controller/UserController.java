package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    /**
     * Creates a user and assigns the default profile (USER).
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(dto));
    }

    /**
     * Lists all users.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    /**
     * Lists users with pagination.
     */
    @GetMapping("/page")
    public ResponseEntity<Page<UserDTO>> listUsersPage(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(userService.listUsersPage(pageable));
    }

    /**
     * Updates user data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto
    ) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * Deletes a user.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}