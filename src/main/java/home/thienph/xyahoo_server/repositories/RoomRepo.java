package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RoomRepo extends JpaRepository<RoomEntity, Long> {

    List<RoomEntity> findAllByExpireAtIsNullOrExpireAtAfter(Date expireAt);
    boolean existsByRoomName(String roomName);
}
