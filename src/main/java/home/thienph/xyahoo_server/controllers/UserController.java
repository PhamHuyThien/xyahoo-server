package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.requests.AddFriendReq;
import home.thienph.xyahoo_server.data.requests.RejectApproveFriendReq;
import home.thienph.xyahoo_server.services.UserService;
import io.netty.channel.Channel;
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
    public void getUserFriendList(Channel channel, Packet packet) {
        int type = packet.getPayload().readInt();
        userService.getUserFriendList(channel, type);
        userService.updateStatusFriend(channel, type);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.REQUEST_ADD_FRIEND)
    public void requestAddFriend(Channel channel, Packet packet) {
        AddFriendReq req = new AddFriendReq().mapping(packet.getPayload());
        userService.requestAddFriend(channel, req);
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.REQUEST_REJECT_APPROVE_FRIEND)
    public void requestRejectApproveFriend(Channel channel, Packet packet) {
        RejectApproveFriendReq req = new RejectApproveFriendReq(packet.getPayload());
        userService.requestRejectApproveFriend(channel, req);
    }
}
