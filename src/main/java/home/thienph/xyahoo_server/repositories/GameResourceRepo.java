package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.GameResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResourceRepo extends JpaRepository<GameResourceEntity, Long> {
    List<GameResourceEntity> findAllByStatus(Integer status);
}
