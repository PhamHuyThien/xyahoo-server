package home.thienph.xyahoo_server.data.users;

import home.thienph.xyahoo_server.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserContext {
    Long id;
    String username;
    UserEntity user;
    UserDevice device;
    boolean isLogin;

    public void destroy() {

    }
}
