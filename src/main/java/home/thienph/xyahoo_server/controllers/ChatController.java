package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.ReceiveChatRoomMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.UserBuzzPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UserChatPacket;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.DestroyScreenProcess;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.services.ChatService;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    GameManager gameManager;

    @Autowired
    HomeCommandService homeCommandService;

    @Autowired
    ChatService chatService;

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.SEND_MESSAGE)
    public void chat(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomKey = XByteBuf.readString(packet.getPayload());
        String message = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getChannels().forEach(userJoinchannel -> {
            ReceiveChatRoomMessagePacket receiveChatRoomMessagePacket = new ReceiveChatRoomMessagePacket(userContext.getUsername(), message, 1);
            userJoinchannel.writeAndFlush(receiveChatRoomMessagePacket.build().getPacket());
        });
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.LEAVE_CHAT_ROOM)
    public void leaveChatRoom(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomKey = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getChannels().remove(channel);
        roomContext.getUsers().remove(userContext);
        roomContext.update();

        homeCommandService.homeSelectRoom(channel, packet.getPayload());

        GameProcessPacketPipeline.newInstance()
                .addPipeline(new DestroyScreenProcess(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID))
                .endPipeline().build().flushPipeline(channel);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_CHAT)
    public void userChat(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        long userId = packet.getPayload().readLong();
        String message = XByteBuf.readString(packet.getPayload());
        Channel userReceiver = gameManager.getChannelByUserId(userId);
        if (userReceiver != null)
            userReceiver.writeAndFlush(new UserChatPacket(userContext.getId(), message).build().getPacket());
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_BUZZ)
    public void userBuzz(Channel channel, Packet packet) {
        long userId = packet.getPayload().readLong();
        UserContext userContext = gameManager.getUserContext(channel);
        Channel userReceiver = gameManager.getChannelByUserId(userId);
        if (userReceiver != null)
            userReceiver.writeAndFlush(new UserBuzzPacket(userContext.getId()).build().getPacket());
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.VIEW_USER_IN_ROOM)
    public void viewUserInRoom(Channel channel, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.showFriendInRoom(channel, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_INVITE_USER)
    public void roomClickInviteUser(Channel channel, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickInviteUser(channel, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ACCEPT_INVITE_ROOM)
    public void acceptInviteRoom(Channel channel, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        String password = XByteBuf.readString(packet.getPayload());
        chatService.acceptInviteJoinRoom(channel, roomKey, password);
    }
}
