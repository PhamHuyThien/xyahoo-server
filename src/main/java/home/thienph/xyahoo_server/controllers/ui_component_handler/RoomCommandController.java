package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.data.mapping.packet.JoinChatRoomPacket;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@UIComponentController
public class RoomCommandController {

    @Autowired
    GameManager gameManager;

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_SELECT_INDEX)
    public void roomSelectIndex(Channel channel, ByteBuf payload) {
        String roomKey = XByteBuf.readString(payload);
        payload.readByte();
        UserContext userContext = gameManager.getUserContext(channel);
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getChannels().add(channel);
        roomContext.getUsers().add(userContext);
        roomContext.update();

        long roomOwnerId = roomContext.getRoom().getUserOwnerId() == null ? -999L : roomContext.getRoom().getUserOwnerId();
        JoinChatRoomPacket joinChatRoomPacket = new JoinChatRoomPacket(true, roomContext.getRoom().getRoomName(), roomKey, roomOwnerId);
        channel.writeAndFlush(joinChatRoomPacket.build().getPacket());
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_CREATE_NEW_ROOM)
    public void roomCreateNewRoom(Channel channel, ByteBuf payload) {
        String roomName = XByteBuf.readString(payload);
        payload.readByte();
        log.info("roomCreateNewRoom {}", roomName);
    }
}
