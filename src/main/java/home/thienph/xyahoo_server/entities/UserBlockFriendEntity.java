package home.thienph.xyahoo_server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_block_friends")
public class UserBlockFriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "username_block", length = 50)
    private String usernameBlock;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

}