package home.thienph.xyahoo_server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users_friends")
public class UsersFriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "friend_username")
    private String friendUsername;

    @Column(name = "friendship_status", length = 20)
    private String friendshipStatus;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "accepted_at")
    private Date acceptedAt;

    @Column(name = "declined_at")
    private Date declinedAt;

    @Column(name = "block_at")
    private Date blockAt;

}