package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

}
