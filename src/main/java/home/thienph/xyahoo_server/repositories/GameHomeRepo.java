package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.GameHomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameHomeRepo extends JpaRepository<GameHomeEntity, Long> {
    List<GameHomeEntity> findAllByStatusOrderByOrderAsc(Integer status);
}
