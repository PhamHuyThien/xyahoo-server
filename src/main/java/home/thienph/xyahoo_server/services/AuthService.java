package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
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

    @SneakyThrows
    public void login(Channel ctx, LoginReq loginReq) {
        if (loginReq.getUsername() == null || loginReq.getPassword() == null) {
            return;
        }
        Channel oldChannel = gameManager.getChannelByUsername(loginReq.getUsername());
        if (oldChannel != null) {
            oldChannel.close();
        }
        UserContext user = gameManager.getUserContext(ctx);
        user.setLogin(true);
        user.setUsername(loginReq.getUsername());
        user.setPassword(loginReq.getPassword());
        resourceService.loadResource(ctx);
        homeService.showHome(ctx);
    }
}
