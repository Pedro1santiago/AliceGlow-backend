package aliceGlow.example.aliceGlow.service;

import aliceGlow.example.aliceGlow.domain.Perfil;
import aliceGlow.example.aliceGlow.domain.User;
import aliceGlow.example.aliceGlow.dto.user.CreateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UpdateUserDTO;
import aliceGlow.example.aliceGlow.dto.user.UserDTO;
import aliceGlow.example.aliceGlow.repository.PerfilRepository;
import aliceGlow.example.aliceGlow.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PerfilRepository perfilRepository;

    public UserService(UserRepository userRepository, PerfilRepository perfilRepository) {
        this.userRepository = userRepository;
        this.perfilRepository = perfilRepository;
    }

    public List<UserDTO> listUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserDTO::toDTO)
                .toList();
    }

    public UserDTO createUser(CreateUserDTO createUserDTO) {
          User user = new User();
          user.setName(createUserDTO.name());
          user.setEmail(createUserDTO.email());
          user.setPassword(createUserDTO.password());

          Perfil userPerfil = perfilRepository.findByName("USER")
                  .orElseThrow(() -> new RuntimeException("Default profile USER not found"));

          user.getPerfils().add(userPerfil);

          User savedUser = userRepository.save(user);

          return UserDTO.toDTO(savedUser);

    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO){
       User user = userRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("User not found"));

       user.setName(updateUserDTO.name());
       user.setEmail(updateUserDTO.email());

       User updatedUser = userRepository.save(user);

       return UserDTO.toDTO(updatedUser);

    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);

    }
}
