package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.UserBlockFriendEntity;
import home.thienph.xyahoo_server.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserBlockFriendRepo extends JpaRepository<UserBlockFriendEntity, Long> {
    @Query("select u from UserBlockFriendEntity ub left join UserEntity u on ub.usernameBlock = u.username where ub.username = :username and ub.status = 1")
    List<UserEntity> findAllUserFriendByUsername(String username);

    @Query("from UserBlockFriendEntity ub where ub.username = :username and ub.usernameBlock = :usernameBlock and ub.status = 1")
    UserBlockFriendEntity findByUsernameAndUsernameBlock(String username, String usernameBlock);
}
