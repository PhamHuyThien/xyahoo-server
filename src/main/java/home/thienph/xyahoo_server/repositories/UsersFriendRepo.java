package home.thienph.xyahoo_server.repositories;

import home.thienph.xyahoo_server.entities.UsersFriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersFriendRepo extends JpaRepository<UsersFriendEntity, Long>, UsersFriendCustomRepo {

    boolean existsByUsernameAndFriendUsername(String username, String friendUsername);

    UsersFriendEntity findFirstByUsernameAndFriendUsername(String username, String friendUsername);

    List<UsersFriendEntity> findAllByUsername(String username);

    List<UsersFriendEntity> findAllByFriendUsername(String friendUsername);

    default UsersFriendEntity findFirstFullByUsernameAndFriendUsername(String username, String friendUsername) {
        UsersFriendEntity usersFriendEntity = findFirstByUsernameAndFriendUsername(username, friendUsername);
        if (usersFriendEntity != null) return usersFriendEntity;
        return findFirstByUsernameAndFriendUsername(friendUsername, username);
    }

    @Query(value = """
            (
                select
                    uf.friend_username as username,
                    uf.friendship_status,
                    uf.create_at,
                    u.status_text,
                    u.id
                from users_friends uf
                join users u on u.username = uf.friend_username
                where uf.username = :username
            )
            union all
            (
                select
                    uf.username as username,
                    uf.friendship_status,
                    uf.create_at,
                    u.status_text,
                    u.id
                from users_friends uf
                join users u on u.username = uf.username
                where uf.friend_username = :username
            )""", nativeQuery = true)
    List<List<Object>> finaAllCustomFullFriendByUsername(String username);

    @Query("select uf From UsersFriendEntity uf left join UserEntity u on uf.username = u.username where uf.friendUsername = ?1 and u.id = ?2")
    UsersFriendEntity findFirstFullByUsernameAndFriendUsername(String username, long friendId);
}
