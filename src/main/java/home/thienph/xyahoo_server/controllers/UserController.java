package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.AddFriendReq;
import home.thienph.xyahoo_server.data.requests.RejectApproveFriendReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @Autowired
    UIComponentHandlerRegistry uiComponentHandlerRegistry;

    @Autowired
    UserService userService;

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_LIST)
    public void getUserFriendList(UserContext userContext, Packet packet) {
        int type = packet.getPayload().readInt();
        userService.getUserFriendList(userContext, type);
        userService.updateStatusFriend(userContext, type);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.REQUEST_ADD_FRIEND)
    public void requestAddFriend(UserContext userContext, Packet packet) {
        AddFriendReq req = new AddFriendReq(packet.getPayload());
        userService.requestAddFriend(userContext, req);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.REQUEST_REJECT_APPROVE_FRIEND)
    public void requestRejectApproveFriend(UserContext userContext, Packet packet) {
        RejectApproveFriendReq req = new RejectApproveFriendReq(packet.getPayload());
        userService.requestRejectApproveFriend(userContext, req);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.DELETE_FRIEND_USER)
    public void deleteFriendUser(UserContext userContext, Packet packet) {
        long userId = packet.getPayload().readLong();
        userService.deleteFriendUser(userContext, userId);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.BLOCK_FRIEND_USER)
    public void blockFriendUser(UserContext userContext, Packet packet) {
        long userId = packet.getPayload().readLong();
        userService.blockFriendUser(userContext, userId);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.UNBLOCK_FRIEND_USER)
    public void unblockFriendUser(UserContext userContext, Packet packet) {
        long userId = packet.getPayload().readLong();
        userService.unblockFriendUser(userContext, userId);
    }
}
