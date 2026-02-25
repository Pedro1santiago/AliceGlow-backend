package aliceGlow.example.aliceGlow.repository;

import aliceGlow.example.aliceGlow.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByActive(boolean active);

	List<Product> findAllByActiveTrue();

	List<Product> findAllByNameContainingIgnoreCase(String name);

	List<Product> findAllByActiveTrueAndNameContainingIgnoreCase(String name);

	List<Product> findAllByActiveAndNameContainingIgnoreCase(boolean active, String name);

	Page<Product> findAllByActive(boolean active, Pageable pageable);

	Page<Product> findAllByActiveTrue(Pageable pageable);

	Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Product> findAllByActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Product> findAllByActiveAndNameContainingIgnoreCase(boolean active, String name, Pageable pageable);
}
