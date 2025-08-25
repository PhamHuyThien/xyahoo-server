package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    UIComponentHandlerRegistry uiComponentHandlerRegistry;

    @PacketMapping(commandId = ClientCommandConst.USER_LIST)
    public void getUserList(Channel channel, Packet packet) {
    }

}
