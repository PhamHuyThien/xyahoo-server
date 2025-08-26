package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.mapping.packet.LoginFailedPacket;
import home.thienph.xyahoo_server.data.mapping.packet.RegisterSuccessPacket;
import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.data.requests.RegisterReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.UserEntity;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.UserRepo;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
    @Autowired
    UserRepo userRepo;

    @SneakyThrows
    public void login(Channel channel, LoginReq loginReq) {
        if (Strings.isBlank(loginReq.getUsername()) || Strings.isBlank(loginReq.getPassword())) return;

        Optional<UserEntity> userEntityOptional = userRepo.findFirstByUsernameAndPassword(loginReq.getUsername(), loginReq.getPassword());
        if (userEntityOptional.isEmpty()) {
            channel.writeAndFlush(new LoginFailedPacket().build().getPacket());
            return;
        }
        Channel oldChannel = gameManager.getChannelByUsername(loginReq.getUsername());
        if (oldChannel != null) oldChannel.close();

        UserContext userContext = gameManager.getUserContext(channel);
        userContext.setUser(userEntityOptional.get());
        userContext.setId(userEntityOptional.get().getId());
        userContext.setUsername(userEntityOptional.get().getUsername());
        userContext.setLogin(true);

        resourceService.loadResource(channel);
        userService.updateUserInfoAndFriendId(channel, userContext);
        homeService.showHome(channel);
    }


    @SneakyThrows
    public void register(Channel channel, RegisterReq loginReq) {
        if (Strings.isBlank(loginReq.getUsername()) || Strings.isBlank(loginReq.getPassword())) return;
        if (userRepo.existsByUsername(loginReq.getUsername())) {
            channel.writeAndFlush(new RegisterSuccessPacket().build().getPacket());
            return;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(loginReq.getUsername());
        userEntity.setPassword(loginReq.getPassword());
        userEntity.setMoney(0L);
        userEntity.setLevel(1);
        userEntity.setRole(UserConstant.ROLE_USER);
        userEntity.setStatus(1);
        userEntity.setCreateTime(new Date());
        userRepo.save(userEntity);
        channel.writeAndFlush(new RegisterSuccessPacket().build().getPacket());
    }
}
