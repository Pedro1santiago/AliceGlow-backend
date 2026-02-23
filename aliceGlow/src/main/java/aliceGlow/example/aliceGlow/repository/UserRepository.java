package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("""
       SELECT u FROM User u
       LEFT JOIN FETCH u.perfils
       WHERE u.email = :email
       """)
    Optional<User> findByEmailWithPerfils(String email);
}
