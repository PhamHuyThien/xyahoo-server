package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<UserEntity> findFirstByUsernameAndPassword(String username, String password);
}
