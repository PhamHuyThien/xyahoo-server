package home.thienph.xyahoo_server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_friend_requests")
public class UserFriendRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "username_request", length = 50)
    private String usernameRequest;

    @ColumnDefault("1")
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

}