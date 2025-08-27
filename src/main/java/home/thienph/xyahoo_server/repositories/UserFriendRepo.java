package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.UserEntity;
import home.thienph.xyahoo_server.entities.UserFriendEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserFriendRepo extends JpaRepository<UserFriendEntity, Long> {
    @Query("select u from UserFriendEntity uf left join UserEntity u on uf.friendUsername = u.username where uf.username = :username and uf.status = 1")
    List<UserEntity> findAllUserFriendByUsername(String username);

    @Query("from UserFriendEntity uf where uf.username = :username and uf.friendUsername = :friendUsername and uf.status = 1")
    UserFriendEntity findByUsernameAndFriendUsername(String username, String friendUsername);

    @Transactional
    @Modifying
    @Query("update UserFriendEntity set status = :status where username = :username and friendUsername = :friendUsername")
    void updateByUsernameAndFriendUsername(String username, String friendUsername, int status);

}
