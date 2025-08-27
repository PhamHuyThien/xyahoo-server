package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.UserEntity;
import home.thienph.xyahoo_server.entities.UserFriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFriendRequestRepo extends JpaRepository<UserFriendRequestEntity, Long> {
    @Query("select u from UserFriendRequestEntity ufr left join UserEntity u on ufr.usernameRequest = u.username where ufr.username = :username and ufr.status = 1")
    List<UserEntity> findAllUserFriendByUsername(String username);

    @Query("from UserFriendRequestEntity ufr where ufr.username = :username and ufr.usernameRequest = :usernameRequest and ufr.status = 1")
    UserFriendRequestEntity findByUsernameAndUsernameRequest(String username, String usernameRequest);
}
