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
import home.thienph.xyahoo_server.data.mapping.packet.game_process.DestroyScreenProcess;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.services.ChatService;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.utils.XByteBuf;
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
    public void chat(UserContext userContext, Packet packet) {

        String roomKey = XByteBuf.readString(packet.getPayload());
        String message = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getUsers().forEach(userJoinUserContext -> {
            ReceiveChatRoomMessagePacket receiveChatRoomMessagePacket = new ReceiveChatRoomMessagePacket(userContext.getUsername(), message, 1);
            userJoinUserContext.getChannel().writeAndFlush(receiveChatRoomMessagePacket.build().getPacket());
        });
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.LEAVE_CHAT_ROOM)
    public void leaveChatRoom(UserContext userContext, Packet packet) {

        String roomKey = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getUsers().remove(userContext);
        roomContext.update();

//        homeCommandService.homeSelectRoom(userContext, packet.getPayload());

        GameProcessPacketPipeline.newInstance()
                .addPipeline(new DestroyScreenProcess(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID))
                .endPipeline().build().flushPipeline(userContext);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_CHAT)
    public void userChat(UserContext userContext, Packet packet) {
        long userId = packet.getPayload().readLong();
        String message = XByteBuf.readString(packet.getPayload());
        chatService.userChat(userContext, userId, message);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_BUZZ)
    public void userBuzz(UserContext userContext, Packet packet) {
        long userId = packet.getPayload().readLong();
        UserContext userReceiver = gameManager.getUserContextByUserId(userId);
        if (userReceiver != null)
            userReceiver.getChannel().writeAndFlush(new UserBuzzPacket(userContext.getUserId()).build().getPacket());
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.VIEW_USER_IN_ROOM)
    public void viewUserInRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.showFriendInRoom(userContext, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_INVITE_USER)
    public void roomClickInviteUser(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickInviteUser(userContext, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ACCEPT_INVITE_ROOM)
    public void acceptInviteRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        String password = XByteBuf.readString(packet.getPayload());
        chatService.acceptInviteJoinRoom(userContext, roomKey, password);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_RENAME_ROOM)
    public void roomClickRenameRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickRenameRoom(userContext, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_CHANGE_PASSWORD_ROOM)
    public void roomClickChangePasswordRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickChangePasswordRoom(userContext, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_EXTEND_ROOM)
    public void roomClickExtendRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickExtendRoom(userContext, roomKey);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.ROOM_DELETE_ROOM)
    public void roomClickDeleteRoom(UserContext userContext, Packet packet) {
        String roomKey = XByteBuf.readString(packet.getPayload());
        chatService.roomClickDeleteRoom(userContext, roomKey);
    }
}
