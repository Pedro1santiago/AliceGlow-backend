package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Perfil;
import aliceGlow.example.aliceGlow.domain.User;
import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.exception.DefaultUserProfileNotFoundException;
import aliceGlow.example.aliceGlow.exception.EmailAlreadyExistsException;
import aliceGlow.example.aliceGlow.exception.UserNotFoundException;
import aliceGlow.example.aliceGlow.repository.PerfilRepository;
import aliceGlow.example.aliceGlow.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PerfilRepository perfilRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PerfilRepository perfilRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lists users.
     */
    public List<UserDTO> listUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserDTO::toDTO)
                .toList();
    }

    /**
     * Lists users with pagination.
     */
    public Page<UserDTO> listUsersPage(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::toDTO);
    }

    /**
     * Creates a user, validates unique email, hashes the password, and assigns the default profile.
     */
    public UserDTO createUser(CreateUserDTO createUserDTO) {

          if (userRepository.existsByEmail(createUserDTO.email())){
              throw new EmailAlreadyExistsException();
          }

          User user = new User();
          user.setName(createUserDTO.name());
          user.setEmail(createUserDTO.email());
          user.setPassword(passwordEncoder.encode(createUserDTO.password()));

          Perfil userPerfil = perfilRepository.findByName("USER")
                  .orElseThrow(DefaultUserProfileNotFoundException::new);

          user.getPerfils().add(userPerfil);

          User savedUser = userRepository.save(user);

          return UserDTO.toDTO(savedUser);

    }

    /**
     * Updates the user's name and email.
     */
    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO){
       User user = userRepository.findById(id)
               .orElseThrow (() -> new UserNotFoundException(id));

       user.setName(updateUserDTO.name());
       user.setEmail(updateUserDTO.email());

       User updatedUser = userRepository.save(user);

       return UserDTO.toDTO(updatedUser);

    }

    /**
     * Deletes a user by id.
     */
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(user);

    }
}
