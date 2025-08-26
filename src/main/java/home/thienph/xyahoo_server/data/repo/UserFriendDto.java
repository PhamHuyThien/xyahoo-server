package home.thienph.xyahoo_server.data.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDto {
    String username;
    String status;
    Date createAt;
    String statusText;
    Long userId;

    public UserFriendDto(List<Object> userFriend) {
        username = userFriend.get(0).toString();
        status = userFriend.get(1).toString();
        createAt = (Date) userFriend.get(2);
        statusText = String.valueOf(userFriend.get(3));
        userId = (Long) userFriend.get(4);
    }
}
