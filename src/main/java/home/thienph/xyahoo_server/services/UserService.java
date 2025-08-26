package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.mapping.packet.FriendListPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UpdateStatusFriendListPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UpdateUserProfilePacket;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    GameManager gameManager;

    public void getUserFriendList(Channel channel) {
        List<BuddyInfo> friends = new ArrayList<>();
        friends.add(new BuddyInfo("DESSC1", "userA", 0));
        friends.add(new BuddyInfo("DESSC2", "userB", 1));
        friends.add(new BuddyInfo("DESSC3", "userC", 2));
        friends.add(new BuddyInfo("DESSC4", "userD", 3));
        friends.add(new BuddyInfo("DESSC5", "userE", 4));
        friends.add(new BuddyInfo("DESSC6", "userF", 5));
        channel.writeAndFlush(new FriendListPacket(friends).build().getPacket());
    }

    public void updateStatusFriend(Channel channel) {
        List<BuddyInfo> friends = new ArrayList<>();
        friends.add(new BuddyInfo("UPDATE2", "userA", 0));
        friends.add(new BuddyInfo("UPDATE1", "userB", 1));
        friends.add(new BuddyInfo("UPDATE3", "userC", 2));
//        friends.add(new BuddyInfo("UPDATE4", "userD", 3));
//        friends.add(new BuddyInfo("5345", "userE", 4));
//        friends.add(new BuddyInfo("vai cac", "userF", 5));
        channel.writeAndFlush(new UpdateStatusFriendListPacket(friends).build().getPacket());
    }

    public void updateUserInfoAndFriendId(Channel channel) {
        UserContext userContext = gameManager.getUserContext(channel);
        UpdateUserProfilePacket updateUserProfilePacket = new UpdateUserProfilePacket(userContext.getAccountId(), null, null);
        channel.writeAndFlush(updateUserProfilePacket.build().getPacket());
    }
}
