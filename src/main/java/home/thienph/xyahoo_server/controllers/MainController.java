package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @Autowired
    UIComponentHandlerRegistry uiComponentHandlerRegistry;

    @PacketMapping(commandId = ClientCommandConst.GET_UI_COMPONENT_DATA)
    public void getDataUIComponent(Channel channel, Packet packet) {
        ByteBuf payload = Unpooled.wrappedBuffer(XByteBuf.readByteArray(packet.getPayload()));
        Integer command = payload.readInt();
        uiComponentHandlerRegistry.handle(channel, command, payload);
    }

    @PacketMapping(commandId = ClientCommandConst.OPEN_SCREEN_BY_ID)
    public void openScreenById(Channel channel, Packet packet) {
        int screenId = packet.getPayload().readInt();
        uiComponentHandlerRegistry.handle(channel, screenId, packet.getPayload());
    }
}
