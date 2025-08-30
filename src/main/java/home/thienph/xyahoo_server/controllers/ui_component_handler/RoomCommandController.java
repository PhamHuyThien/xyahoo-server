package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.data.requests.AddFriendReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.services.ChatService;
import home.thienph.xyahoo_server.services.UserService;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.services.ui_component_handler.RoomCommandService;
import home.thienph.xyahoo_server.utils.XByteBuf;
import home.thienph.xyahoo_server.utils.XPacket;
import io.netty.buffer.ByteBuf;
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
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_SELECT_INDEX)
    public void joinChatRoom(UserContext userContext, ByteBuf payload) {
        String roomKey = XByteBuf.readString(payload);
        payload.readByte();
        roomCommandService.joinChatRoom(userContext, roomKey, null);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_CREATE_NEW_ROOM)
    public void roomCreateNewRoom(UserContext userContext, ByteBuf payload) {
        String roomName = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomCreateNewRoom(userContext, roomName);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_REFRESH_ROOM)
    public void refreshRoom(UserContext userContext, ByteBuf payload) {
        payload.readInt();
        payload.readByte();
        homeCommandService.homeSelectRoom(userContext, payload);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_ADD_FRIEND)
    public void roomAddFriend(UserContext userContext, ByteBuf payload) {

        String username = XByteBuf.readString(payload);
        payload.readByte();
        if (username == null || username.trim().isEmpty()) return;
        if (userContext.getUsername().equals(username)) {
            XPacket.showSimpleDialog(userContext, "Không thể kết bạn với chinh mình");
            return;
        }
        userService.requestAddFriend(userContext, new AddFriendReq(username));
        XPacket.showSimpleMarquee(userContext, "Đã gửi yêu cầu kết bạn tới " + username);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_REFRESH_LIST)
    public void roomRefreshListUserInRoom(UserContext userContext, ByteBuf payload) {

        String username = XByteBuf.readString(payload);
        payload.readByte();
        roomCommandService.refreshListUserInRoom(userContext, username);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_ADD_USER_IN_ROOM)
    public void roomAddUserInRoom(UserContext userContext, ByteBuf payload) {
        String usernameInvite = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomAddUserInRoom(userContext, usernameInvite);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_KICK_USER)
    public void roomKickUser(UserContext userContext, ByteBuf payload) {

        String usernameKick = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomKickUserInRoom(userContext, usernameKick);
        roomCommandService.refreshListUserInRoom(userContext, userContext.getUsername());
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_RENAME_ROOM)
    public void roomRenameRoom(UserContext userContext, ByteBuf payload) {
        String newRoomName = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomRenameRoom(userContext, newRoomName);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_CHANGE_PASSWORD_ROOM)
    public void roomChangePassword(UserContext userContext, ByteBuf payload) {
        String newPasswordRoom = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomChangePassword(userContext, newPasswordRoom);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_INPUT_PASSWORD_ROOM)
    public void handlerRoomInputPassword(UserContext userContext, ByteBuf payload) {
        String passwordRoom = XByteBuf.readString(payload).trim();
        String roomKey = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.handlerRoomInputPassword(userContext, roomKey, passwordRoom);
    }


}
