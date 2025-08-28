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
    @Autowired
    UserService userService;
    @Autowired
    ChatService chatService;

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_SELECT_INDEX)
    public void joinChatRoom(Channel channel, ByteBuf payload) {
        String roomKey = XByteBuf.readString(payload);
        payload.readByte();
        roomCommandService.joinChatRoom(channel, roomKey);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_CREATE_NEW_ROOM)
    public void roomCreateNewRoom(Channel channel, ByteBuf payload) {
        String roomName = XByteBuf.readString(payload).trim();
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

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_ADD_FRIEND)
    public void roomAddFriend(Channel channel, ByteBuf payload) {
        UserContext userContext = gameManager.getUserContext(channel);
        String username = XByteBuf.readString(payload);
        payload.readByte();
        if (username == null || username.trim().isEmpty()) return;
        if (userContext.getUsername().equals(username)) {
            XPacket.showSimpleDialog(channel, "Không thể kết bạn với chinh mình");
            return;
        }
        userService.requestAddFriend(channel, new AddFriendReq(username));
        XPacket.showSimpleMarquee(channel, "Đã gửi yêu cầu kết bạn tới " + username);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_REFRESH_LIST)
    public void roomRefreshListUserInRoom(Channel channel, ByteBuf payload) {
        UserContext userContext = gameManager.getUserContext(channel);
        String username = XByteBuf.readString(payload);
        payload.readByte();
        roomCommandService.refreshListUserInRoom(channel, username);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_ADD_USER_IN_ROOM)
    public void roomAddUserInRoom(Channel channel, ByteBuf payload) {
        String usernameInvite = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomAddUserInRoom(channel, usernameInvite);
    }

    @SneakyThrows
    @CommandMapping(commandId = CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_KICK_USER)
    public void roomKickUser(Channel channel, ByteBuf payload) {
        UserContext userContext = gameManager.getUserContext(channel);
        String usernameKick = XByteBuf.readString(payload).trim();
        payload.readByte();
        roomCommandService.roomKickUserInRoom(channel, usernameKick);
        roomCommandService.refreshListUserInRoom(channel, userContext.getUsername());
    }
}
