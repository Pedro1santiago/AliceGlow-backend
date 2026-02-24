package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByActive(boolean active);

	List<Product> findAllByActiveTrue();
}
