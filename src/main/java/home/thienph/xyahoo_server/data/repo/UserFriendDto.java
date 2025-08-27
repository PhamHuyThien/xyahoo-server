package home.thienph.xyahoo_server.data.repo;

import home.thienph.xyahoo_server.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDto {
    Long userId;
    String username;
    String status;
    String statusText;

    public UserFriendDto(UserEntity userEntity) {
        userId = userEntity.getId();
        username = userEntity.getUsername();
        status = userEntity.getStatusText();
        statusText = userEntity.getStatusText();
    }
}
