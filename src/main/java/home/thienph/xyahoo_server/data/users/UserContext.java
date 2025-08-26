package home.thienph.xyahoo_server.data.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserContext {
    Long accountId;
    String username;
    String password;
    String role;
    boolean isLogin;
    UserDevice device;


    public void destroy() {

    }
}
