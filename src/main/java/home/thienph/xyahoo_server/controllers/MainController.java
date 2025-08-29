package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {

    @Autowired
    UIComponentHandlerRegistry uiComponentHandlerRegistry;

    @PacketMapping(commandId = ClientCommandConst.GET_UI_COMPONENT_DATA)
    @HasRole({UserConstant.ROLE_USER})
    public void getDataUIComponent(UserContext userContext, Packet packet) {
        ByteBuf payload = Unpooled.wrappedBuffer(XByteBuf.readByteArray(packet.getPayload()));
        Integer command = payload.readInt();
        uiComponentHandlerRegistry.handle(userContext, command, payload);
    }

    @PacketMapping(commandId = ClientCommandConst.OPEN_SCREEN_BY_ID)
    @HasRole({UserConstant.ROLE_USER})
    public void openScreenById(UserContext userContext, Packet packet) {
        int screenId = packet.getPayload().readInt();
        uiComponentHandlerRegistry.handle(userContext, screenId, packet.getPayload());
    }
}
