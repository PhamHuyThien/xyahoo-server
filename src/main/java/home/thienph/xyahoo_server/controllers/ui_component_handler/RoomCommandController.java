package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.data.mapping.packet.JoinChatRoomPacket;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.services.ui_component_handler.RoomCommandService;
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
    @Autowired
    HomeCommandService homeCommandService;
    @Autowired
    RoomCommandService roomCommandService;

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_SELECT_INDEX)
    public void joinChatRoom(Channel channel, ByteBuf payload) {
        String roomKey = XByteBuf.readString(payload);
        payload.readByte();

        if (roomKey == null || roomKey.isEmpty()) return;

        UserContext userContext = gameManager.getUserContext(channel);
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        if (!roomContext.getChannels().contains(channel)) {
            roomContext.getChannels().add(channel);
            roomContext.getUsers().add(userContext);
            roomContext.update();
        }

        long roomOwnerId = roomContext.getRoom().getUserOwnerId() == null ? -999L : roomContext.getRoom().getUserOwnerId();
        JoinChatRoomPacket joinChatRoomPacket = new JoinChatRoomPacket(true, roomContext.getRoom().getRoomName(), roomKey, roomOwnerId);
        channel.writeAndFlush(joinChatRoomPacket.build().getPacket());
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_CREATE_NEW_ROOM)
    public void roomCreateNewRoom(Channel channel, ByteBuf payload) {
        String roomName = XByteBuf.readString(payload);
        payload.readByte();
        roomCommandService.roomCreateNewRoom(channel, roomName);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_REFRESH_ROOM)
    public void refreshRoom(Channel channel, ByteBuf payload) {
        payload.readInt();
        payload.readByte();
        homeCommandService.homeSelectRoom(channel, payload);
    }
}
