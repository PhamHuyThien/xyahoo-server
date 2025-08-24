package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.requests.LoginReq;
import io.netty.channel.ChannelHandlerContext;
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


    @SneakyThrows
    public void login(ChannelHandlerContext ctx, LoginReq loginReq) {
        //
//        simulatorService.outputLoginSuccess(ctx);
        simulatorService.processData(ctx);
    }
}
