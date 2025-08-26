package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.utils.XBase64;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    @Autowired
    SimulatorService simulatorService;


    @Autowired
    GameManager gameManager;

    @Autowired
    ResourceService resourceService;

    @Autowired
    HomeService homeService;

    @Autowired
    UserService userService;

    @SneakyThrows
    public void login(Channel ctx, LoginReq loginReq) {
        if (loginReq.getUsername() == null || loginReq.getPassword() == null) {
            return;
        }
        String username = XBase64.decodeWithReverse(loginReq.getUsername());
        String password = XBase64.decodeWithReverse(loginReq.getPassword());

        Channel oldChannel = gameManager.getChannelByUsername(username);
        if (oldChannel != null) {
            oldChannel.close();
        }
        UserContext user = gameManager.getUserContext(ctx);
        user.setAccountId(1L);
        user.setLogin(true);
        user.setUsername(username);
        user.setPassword(password);
        resourceService.loadResource(ctx);
        userService.updateUserInfoAndFriendId(ctx);
        homeService.showHome(ctx);
    }
}
