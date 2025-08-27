package home.thienph.xyahoo_server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "room_group")
public class RoomGroupEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "group_name", length = 225)
    private String groupName;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

}