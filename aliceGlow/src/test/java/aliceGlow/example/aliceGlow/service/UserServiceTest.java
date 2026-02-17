package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Perfil;
import aliceGlow.example.aliceGlow.domain.User;
import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.exception.UserNotFoundException;
import aliceGlow.example.aliceGlow.repository.PerfilRepository;
import aliceGlow.example.aliceGlow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PerfilRepository perfilRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void shouldListUsers(){

        Perfil perfil = new Perfil();
        perfil.setName("USER");

        User user = new User();
        user.setName("Diana");
        user.setEmail("diana@gmail.com");
        user.setPassword("12345ps");
        user.setPerfils(Set.of(perfil));

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDTO> result = userService.listUsers();

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals("Diana", result.get(0).name());
        assertEquals("diana@gmail.com", result.get(0).email());

        verify(userRepository).findAll();
    }

    @Test
    void shouldCreateUserSuccessfully(){

        CreateUserDTO dto = new CreateUserDTO(
                "Diana",
                "diana@gmail.com",
                "12345pS",
                new HashSet<>()
        );

        Perfil perfil = new Perfil();
        perfil.setName("USER");

        when(perfilRepository.findByName("USER"))
                .thenReturn(Optional.of(perfil));

        User user = new User();
        user.setName("Diana");
        user.setEmail("diana@gmail.com");
        user.setPassword(passwordEncoder.encode("12345ps"));
        user.setPerfils(Set.of(perfil));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("Diana", result.name());
        assertEquals("diana@gmail.com", result.email());

        verify(perfilRepository).findByName("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldUpdateUserSuccessfully(){

        Long userId = 1L;

        Perfil perfil = new Perfil();
        perfil.setName("USER");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Diana");
        existingUser.setEmail("diana@gmail.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Diana");
        updatedUser.setEmail("dianas@gmail.com");
        updatedUser.setPassword(passwordEncoder.encode("12345ps"));
        updatedUser.setPerfils(Set.of(perfil));

        UpdateUserDTO dto = new UpdateUserDTO(
                "Diana",
                "dianas@gmail.com");

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenReturn(updatedUser);

        UserDTO result = userService.updateUser(userId,dto);

        assertNotNull(result);
        assertEquals("Diana", result.name());
        assertEquals("dianas@gmail.com", result.email());

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class ));
    }

    @Test
    void shouldDeleteUserSuccessfully(){

        Long userId = 1L;

        Perfil perfil = new Perfil();
        perfil.setName("USER");

        User user = new User();
        user.setId(userId);
        user.setName("Diana");
        user.setEmail("diana@gmail.com");
        user.setPassword(passwordEncoder.encode("12345ps"));
        user.setPerfils(Set.of(perfil));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository).delete(user);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnDelete(){

        Long userId = 1L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals("User not found with id: " + userId, exception.getMessage());

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){

        CreateUserDTO dto = new CreateUserDTO(
                "Diana",
                "diana@gmail.com",
                "12345pS",
                new HashSet<>()
        );

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(dto)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).existsByEmail(dto.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDefaultProfileNotFound(){

        CreateUserDTO dto = new CreateUserDTO(
                "Diana",
                "diana@gmail.com",
                "12345pS",
                new HashSet<>()
        );

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(perfilRepository.findByName("USER"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(dto)
        );

        assertEquals("Default profile USER not found", exception.getMessage());

        verify(perfilRepository).findByName("USER");
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldEncodePasswordBeforeSavingUser(){

        CreateUserDTO dto = new CreateUserDTO(
                "Diana",
                "diana@gmail.com",
                "12345pS",
                new HashSet<>()
        );

        Perfil perfil = new Perfil();
        perfil.setName("USER");

        when(userRepository.existsByEmail(dto.email()))
                .thenReturn(false);

        when(perfilRepository.findByName("USER"))
                .thenReturn(Optional.of(perfil));

        when(passwordEncoder.encode(dto.password()))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);

        verify(passwordEncoder).encode(dto.password());
        verify(userRepository).save(any(User.class));
    }




}
