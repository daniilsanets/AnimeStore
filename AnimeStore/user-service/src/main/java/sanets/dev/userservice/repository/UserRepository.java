package sanets.dev.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sanets.dev.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
