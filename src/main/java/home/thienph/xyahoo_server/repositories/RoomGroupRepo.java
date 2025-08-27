package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import home.thienph.xyahoo_server.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomGroupRepo extends JpaRepository<RoomGroupEntity, Long> {
}
