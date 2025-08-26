package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.data.mapping.packet.JoinChatRoomPacket;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@UIComponentController
public class RoomCommandController {

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_SELECT_INDEX)
    public void roomSelectIndex(Channel channel, ByteBuf payload) {
        String roomIndex = XByteBuf.readString(payload);
        payload.readByte();
        log.info("roomSelectIndex {}", roomIndex);

//        ShowInviteChatDialogPacket packet = new ShowInviteChatDialogPacket("test1", "test2", "test3", "test4");
//        channel.writeAndFlush(packet.build().getPacket());

        JoinChatRoomPacket joinChatRoomPacket = new JoinChatRoomPacket(true, "test1", "roomID", 1);
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
