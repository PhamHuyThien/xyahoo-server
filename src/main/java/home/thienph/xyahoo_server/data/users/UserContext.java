package home.thienph.xyahoo_server.data.users;

import home.thienph.xyahoo_server.entities.UserEntity;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserContext {
    Channel channel;
    String channelId;
    Long userId;
    String username;
    UserEntity user;
    UserDevice device;
    boolean isLogin;

    public UserContext(Channel channel) {
        this.channel = channel;
        this.channelId = channel.id().asShortText();
    }

    public void destroy() {
        if(channel != null && channel.isActive()) channel.close();
    }
}
