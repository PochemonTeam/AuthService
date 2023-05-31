package pochemon.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pochemon.auth.entity.Login;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, String> {
    Optional<Login> findLoginByUsername(String username);
}
