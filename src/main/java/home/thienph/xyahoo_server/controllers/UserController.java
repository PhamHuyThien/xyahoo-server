package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.configs.UIComponentHandlerRegistry;
import home.thienph.xyahoo_server.constants.ClientCommandConst;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.mapping.packet.FriendListPacket;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UIComponentHandlerRegistry uiComponentHandlerRegistry;

    @PacketMapping(commandId = ClientCommandConst.USER_LIST)
    public void getUserList(Channel channel, Packet packet) {
        List<BuddyInfo> friends = new ArrayList<>();
        friends.add(new BuddyInfo("DESSC1", "userA", 0));
        friends.add(new BuddyInfo("DESSC2", "userB", 1));
        friends.add(new BuddyInfo("DESSC3", "userC", 2));
        friends.add(new BuddyInfo("DESSC4", "userD", 3));
        friends.add(new BuddyInfo("DESSC5", "userE", 4));
        friends.add(new BuddyInfo("DESSC6", "userF", 5));
        channel.writeAndFlush(new FriendListPacket(friends).build().getPacket());
    }

}
