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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
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
                "Maria Santos Updated",
                "maria.updated@example.com"
        );
    }

    @Test
    @DisplayName("Should create user with status 201 CREATED")
    void shouldCreateUserSuccessfully() {
        UserDTO createdUserDTO = new UserDTO(
                1L,
                "Maria Santos",
                "maria@example.com",
                Set.of()
        );

        when(userService.createUser(any(CreateUserDTO.class)))
                .thenReturn(createdUserDTO);

        ResponseEntity<UserDTO> response = userController.createUser(createUserDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Maria Santos", response.getBody().name());
        assertEquals("maria@example.com", response.getBody().email());

        verify(userService, times(1)).createUser(any(CreateUserDTO.class));
    }

    @Test
    @DisplayName("Should call userService.createUser with correct DTO")
    void shouldCallCreateUserWithCorrectDTO() {
        when(userService.createUser(any(CreateUserDTO.class)))
                .thenReturn(userDTO);

        userController.createUser(createUserDTO);

        verify(userService).createUser(any(CreateUserDTO.class));
    }

    @Test
    @DisplayName("Should return created user with all required fields")
    void shouldReturnCreatedUserWithAllFields() {
        when(userService.createUser(any(CreateUserDTO.class)))
                .thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(createUserDTO);

        assertEquals(1L, response.getBody().id());
        assertEquals("Ana Silva", response.getBody().name());
        assertEquals("ana@example.com", response.getBody().email());
    }

    @Test
    @DisplayName("Should list all users with status 200 OK")
    void shouldListUsersSuccessfully() {
        UserDTO userDTO2 = new UserDTO(
                2L,
                "João Silva",
                "joao@example.com",
                Set.of()
        );

        when(userService.listUsers())
                .thenReturn(List.of(userDTO, userDTO2));

        ResponseEntity<List<UserDTO>> response = userController.listUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Ana Silva", response.getBody().get(0).name());
        assertEquals("João Silva", response.getBody().get(1).name());

        verify(userService, times(1)).listUsers();
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsers() {
        when(userService.listUsers())
                .thenReturn(List.of());

        ResponseEntity<List<UserDTO>> response = userController.listUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());

        verify(userService).listUsers();
    }

    @Test
    @DisplayName("Should update user with status 200 OK")
    void shouldUpdateUserSuccessfully() {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO(
                userId,
                "Maria Santos Updated",
                "maria.updated@example.com",
                Set.of()
        );

        when(userService.updateUser(eq(userId), any(UpdateUserDTO.class)))
                .thenReturn(updatedUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, updateUserDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Maria Santos Updated", response.getBody().name());
        assertEquals("maria.updated@example.com", response.getBody().email());

        verify(userService, times(1)).updateUser(eq(userId), any(UpdateUserDTO.class));
    }

    @Test
    @DisplayName("Should call userService.updateUser with correct parameters")
    void shouldCallUpdateUserWithCorrectParameters() {
        Long userId = 1L;
        when(userService.updateUser(eq(userId), any(UpdateUserDTO.class)))
                .thenReturn(userDTO);

        userController.updateUser(userId, updateUserDTO);

        verify(userService).updateUser(eq(userId), any(UpdateUserDTO.class));
    }

    @Test
    @DisplayName("Should return updated user with correct data")
    void shouldReturnUpdatedUserWithCorrectData() {
        Long userId = 1L;
        UserDTO updatedUserDTO = new UserDTO(
                userId,
                "Updated Name",
                "updated@example.com",
                Set.of()
        );

        when(userService.updateUser(eq(userId), any(UpdateUserDTO.class)))
                .thenReturn(updatedUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(userId, updateUserDTO);

        assertEquals("Updated Name", response.getBody().name());
        assertEquals("updated@example.com", response.getBody().email());
    }

    @Test
    @DisplayName("Should delete user with status 204 NO CONTENT")
    void shouldDeleteUserSuccessfully() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(userService, times(1)).deleteUser(userId);
        verify(userService, never()).listUsers();
    }

    @Test
    @DisplayName("Should call userService.deleteUser with correct ID")
    void shouldCallDeleteUserWithCorrectId() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        userController.deleteUser(userId);

        verify(userService).deleteUser(eq(userId));
    }

    @Test
    @DisplayName("Should delete user and return no content")
    void shouldDeleteUserAndReturnNoContent() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
