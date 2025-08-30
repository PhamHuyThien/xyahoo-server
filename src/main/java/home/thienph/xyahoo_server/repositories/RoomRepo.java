package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RoomRepo extends JpaRepository<RoomEntity, Long> {

    @Query("select r from RoomEntity r left join RoomGroupEntity rg on r.roomGroupId = rg.id where r.isDelete = 0 and (r.expireAt is null or r.expireAt > :expireAt)")
    List<RoomEntity> findAllRoomOpen(Date expireAt);
    boolean existsByRoomName(String roomName);
}
