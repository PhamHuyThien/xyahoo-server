package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.LoginReq;
import home.thienph.xyahoo_server.services.AuthService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    AuthService authService;

    @PacketMapping(commandId = ClientCommandConst.LOGIN)
    public void login(Channel channel, Packet packet) {
        LoginReq req = new LoginReq().mapping(packet.getPayload());
        authService.login(channel, req);
    }
}
