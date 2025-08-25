package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import io.netty.channel.Channel;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @PacketMapping(commandId = ClientCommandConst.GET_UI_COMPONENT_DATA)
    public void getDataUIComponent(Channel channel, Packet packet) {
    }

}
