package home.thienph.xyahoo_server.commands;

import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.services.AuthService;
import home.thienph.xyahoo_server.services.SimulatorService;
import io.netty.channel.ChannelHandlerContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("xYahooCommand")
@Slf4j
public class XYahooCommand implements Command {


    @Autowired
    AuthService authService;

    @Autowired
    SimulatorService simulatorService;

    @Override
    @SneakyThrows
    public void execute(ChannelHandlerContext ctx, Packet packet) {
        switch (packet.getCommandId()) {
            case ClientCommandConst.LOGIN:

                LoginReq loginReq = new LoginReq().mapping(packet.getPayload());
                authService.login(ctx, loginReq);

                break;
            case ClientCommandConst.LOGIN_YAHOO:
//                simulatorService.loginYahooSuccess(ctx);
                simulatorService.listFriendYahoo(ctx);
                break;
            case 5018:
//                simulatorService.showPopupDialog(ctx);
//                simulatorService.moiVaoPhong(ctx);
//                simulatorService.buzz(ctx);
                simulatorService.moiVaoPhongChat(ctx);
                Thread.sleep(3000);
                simulatorService.danhSachBanTLMN(ctx);
//                simulatorService.listFriend(ctx);
//                simulatorService.handleGameChat(ctx);
                break;
        }
    }
}
