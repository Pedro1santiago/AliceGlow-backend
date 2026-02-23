package aliceGlow.example.aliceGlow.infra.security;

import aliceGlow.example.aliceGlow.domain.Perfil;
import aliceGlow.example.aliceGlow.domain.User;
import aliceGlow.example.aliceGlow.exception.UserNotFoundException;
import aliceGlow.example.aliceGlow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmailWithPerfils(email)
                .orElseThrow(UserNotFoundException::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getPerfils().stream()
                        .map(Perfil::getName)
                        .toArray(String[]::new))
                .build();
    }
}