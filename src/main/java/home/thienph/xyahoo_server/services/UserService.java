package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.mapping.packet.AreYouAddFriendPacket;
import home.thienph.xyahoo_server.data.mapping.packet.FriendListPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UpdateStatusFriendListPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UpdateUserProfilePacket;
import home.thienph.xyahoo_server.data.repo.UserFriendDto;
import home.thienph.xyahoo_server.data.requests.AddFriendReq;
import home.thienph.xyahoo_server.data.requests.RejectApproveFriendReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.UserEntity;
import home.thienph.xyahoo_server.entities.UsersFriendEntity;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.UserRepo;
import home.thienph.xyahoo_server.repositories.UsersFriendRepo;
import io.netty.channel.Channel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    GameManager gameManager;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private UsersFriendRepo usersFriendRepo;

    public void getUserFriendList(Channel channel, int type) { //1 banbe, 2 tu choi, 3 moi ket ban
        UserContext userContext = gameManager.getUserContext(channel);
        List<UserFriendDto> userFriends = getUserFriendByType(userContext, type);
        channel.writeAndFlush(new FriendListPacket(userFriends).build().getPacket());
    }

    public void updateStatusFriend(Channel channel, int type) {
        UserContext userContext = gameManager.getUserContext(channel);
        List<UserFriendDto> userFriends = getUserFriendByType(userContext, type);
        List<UserFriendDto> usersFriendOnline = new ArrayList<>();
        for (UserFriendDto userFriendDto : userFriends) {
            if (gameManager.getOptionalChannelByUsername(userFriendDto.getUsername()).isPresent()) {
                usersFriendOnline.add(userFriendDto);
            }
        }
        channel.writeAndFlush(new UpdateStatusFriendListPacket(usersFriendOnline).build().getPacket());
    }

    public void updateUserInfoAndFriendId(Channel channel, UserContext userContext) {
        UpdateUserProfilePacket updateUserProfilePacket = new UpdateUserProfilePacket(userContext.getId(), null, null);
        channel.writeAndFlush(updateUserProfilePacket.build().getPacket());
    }

    public void requestAddFriend(Channel channel, AddFriendReq req) {
        UserContext userContext = gameManager.getUserContext(channel);
        if (userContext == null || req == null || Strings.isBlank(req.getUsername())) return;
        Optional<UserEntity> userFriend = userRepo.findByUsername(req.getUsername());
        if (userFriend.isEmpty()) return;
        if (usersFriendRepo.findFirstFullByUsernameAndFriendUsername(userContext.getUsername(), userFriend.get().getUsername()) != null)
            return;

        UsersFriendEntity usersFriendEntity = new UsersFriendEntity();
        usersFriendEntity.setUsername(userContext.getUsername());
        usersFriendEntity.setFriendUsername(userFriend.get().getUsername());
        usersFriendEntity.setFriendshipStatus(UserConstant.FRIEND_PENDING_STATUS);
        usersFriendEntity.setCreateAt(new Date());
        usersFriendRepo.save(usersFriendEntity);

        Optional<Channel> userFriendChannel = gameManager.getOptionalChannelByUsername(userFriend.get().getUsername());
        userFriendChannel.ifPresent(value -> value.writeAndFlush(new AreYouAddFriendPacket(userContext.getUsername(), userContext.getId()).build().getPacket()));
    }

    public void requestRejectApproveFriend(Channel channel, RejectApproveFriendReq req) {
        UserContext userContext = gameManager.getUserContext(channel);
        UsersFriendEntity usersFriendEntity = usersFriendRepo.findFirstFullByUsernameAndFriendUsername(userContext.getUsername(), req.getUserId());
        if (usersFriendEntity == null || !usersFriendEntity.getFriendshipStatus().equals(UserConstant.FRIEND_PENDING_STATUS))
            return;
        if (req.isApprove()) {
            usersFriendEntity.setFriendshipStatus(UserConstant.FRIEND_ACCEPTED_STATUS);
            usersFriendRepo.save(usersFriendEntity);
        } else {
            usersFriendRepo.delete(usersFriendEntity);
        }
    }

    public List<UserFriendDto> getFriendList(UserContext userContext) {
        return usersFriendRepo
                .finaAllCustomFullFriendByUsername(userContext.getUser().getUsername())
                .stream().map(UserFriendDto::new)
                .toList();
    }

    public List<UserFriendDto> getFriendListAccepted(UserContext userContext) {
        return getFriendList(userContext)
                .stream().filter(userFriendDto -> userFriendDto.getStatus().equals(UserConstant.FRIEND_ACCEPTED_STATUS))
                .toList();
    }

    public List<UserFriendDto> getFriendListBlocked(UserContext userContext) {
        return getFriendList(userContext)
                .stream().filter(userFriendDto -> userFriendDto.getStatus().equals(UserConstant.FRIEND_BLOCKED_STATUS))
                .toList();
    }

    public List<UserFriendDto> getFriendListPending(UserContext userContext) {
        return getFriendList(userContext)
                .stream().filter(userFriendDto -> userFriendDto.getStatus().equals(UserConstant.FRIEND_PENDING_STATUS))
                .toList();
    }

    public List<UserFriendDto> getUserFriendByType(UserContext userContext, int type) {
        List<UserFriendDto> userFriends = new ArrayList<>();
        if (type == 1) {
            userFriends = getFriendListAccepted(userContext);
        } else if (type == 2) {
            userFriends = getFriendListBlocked(userContext);
        } else if (type == 3) {
            userFriends = getFriendListPending(userContext);
        }
        return userFriends;
    }
}
