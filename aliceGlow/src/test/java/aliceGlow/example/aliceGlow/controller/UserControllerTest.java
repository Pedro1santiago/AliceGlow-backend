package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(
                1L,
                "Ana Silva",
                "ana@example.com",
                Set.of()
        );

        createUserDTO = new CreateUserDTO(
                "Maria Santos",
                "maria@example.com",
                "password123",
                Set.of()
        );

        updateUserDTO = new UpdateUserDTO(
                "Maria Updated",
                "updated@example.com"
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        when(userService.createUser(any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(createUserDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).createUser(any());
    }

    @Test
    void shouldListUsersSuccessfully() {
        when(userService.listUsers()).thenReturn(List.of(userDTO));

        ResponseEntity<List<UserDTO>> response = userController.listUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService).listUsers();
    }

    @Test
    void shouldListUsersPageSuccessfully() {
        Page<UserDTO> page = new PageImpl<>(List.of(userDTO), PageRequest.of(0, 20), 1);
        when(userService.listUsersPage(PageRequest.of(0, 20))).thenReturn(page);

        ResponseEntity<Page<UserDTO>> response = userController.listUsersPage(PageRequest.of(0, 20));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(userService).listUsersPage(PageRequest.of(0, 20));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userService.updateUser(eq(1L), any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response =
                userController.updateUser(1L, updateUserDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).updateUser(eq(1L), any());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}
