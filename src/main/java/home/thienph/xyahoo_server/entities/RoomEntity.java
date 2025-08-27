package home.thienph.xyahoo_server.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "max_user")
    private Integer maxUser;

    @Column(name = "password")
    private String password;

    @Column(name = "room_key")
    private String roomKey;

    @Column(name = "icon_id")
    private Integer iconId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "user_owner_id")
    private Long userOwnerId;

    @Column(name = "expire_at")
    private Date expireAt;

    @Column(name = "room_id")
    private Long roomId;

}