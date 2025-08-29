package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.UserConstant;
import home.thienph.xyahoo_server.data.mapping.packet.*;
import home.thienph.xyahoo_server.data.repo.UserFriendDto;
import home.thienph.xyahoo_server.data.requests.AddFriendReq;
import home.thienph.xyahoo_server.data.requests.RejectApproveFriendReq;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.UserBlockFriendEntity;
import home.thienph.xyahoo_server.entities.UserEntity;
import home.thienph.xyahoo_server.entities.UserFriendEntity;
import home.thienph.xyahoo_server.entities.UserFriendRequestEntity;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.UserBlockFriendRepo;
import home.thienph.xyahoo_server.repositories.UserFriendRepo;
import home.thienph.xyahoo_server.repositories.UserFriendRequestRepo;
import home.thienph.xyahoo_server.repositories.UserRepo;
import home.thienph.xyahoo_server.utils.XPacket;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    GameManager gameManager;
    @Autowired
    UserRepo userRepo;
    @Autowired
    private UserFriendRequestRepo userFriendRequestRepo;
    @Autowired
    private UserFriendRepo userFriendRepo;
    @Autowired
    UserBlockFriendRepo userBlockFriendRepo;

    public void getUserFriendList(UserContext userContext, int type) { //1 banbe, 2 tu choi, 3 moi ket ban
        
        List<UserFriendDto> userFriends = getUserFriendByType(userContext, type)
                .stream().map(UserFriendDto::new)
                .toList();
        userContext.getChannel().writeAndFlush(new FriendListPacket(userFriends).build().getPacket());
    }

    public void updateStatusFriend(UserContext userContext, int type) {
        
        List<UserFriendDto> userFriends = getUserFriendByType(userContext, type)
                .stream().map(UserFriendDto::new)
                .toList();
        List<UserFriendDto> usersFriendOnline = new ArrayList<>();
        for (UserFriendDto userFriendDto : userFriends) {
            if (gameManager.getUserContextByUsername(userFriendDto.getUsername()) != null) {
                usersFriendOnline.add(userFriendDto);
            }
        }
        userContext.getChannel().writeAndFlush(new UpdateStatusFriendListPacket(usersFriendOnline).build().getPacket());
    }

    public void updateUserInfoAndFriendId(UserContext userContext) {
        UpdateUserProfilePacket updateUserProfilePacket = new UpdateUserProfilePacket(userContext.getUserId(), null, null);
        userContext.getChannel().writeAndFlush(updateUserProfilePacket.build().getPacket());
    }

    public void requestAddFriend(UserContext userContext, AddFriendReq req) {
        
        if (userContext == null || req == null || Strings.isBlank(req.getUsername())) return;
        UserEntity userWantFriend = userRepo.findByUsername(req.getUsername()).orElse(null);
        if (userWantFriend == null) return;
        if (userFriendRequestRepo.findByUsernameAndUsernameRequest(userContext.getUsername(), userWantFriend.getUsername()) != null
            || userFriendRequestRepo.findByUsernameAndUsernameRequest(userWantFriend.getUsername(), userContext.getUsername()) != null) {
            XPacket.showSimpleDialog(userContext, "Bạn hoặc đối phương đã gửi yêu cầu kết bạn");
            return;
        }
        if (userBlockFriendRepo.findByUsernameAndUsernameBlock(userContext.getUsername(), userWantFriend.getUsername()) != null
            || userBlockFriendRepo.findByUsernameAndUsernameBlock(userWantFriend.getUsername(), userContext.getUsername()) != null){
            XPacket.showSimpleDialog(userContext, "Không thể kết bạn với người này");
            return;
        }
        UserFriendRequestEntity userFriendRequestEntity = new UserFriendRequestEntity();
        userFriendRequestEntity.setUsername(userWantFriend.getUsername());
        userFriendRequestEntity.setUsernameRequest(userContext.getUsername());
        userFriendRequestEntity.setStatus(1);
        userFriendRequestEntity.setCreateAt(new Date());
        userFriendRequestRepo.save(userFriendRequestEntity);

        UserContext userWantFriendContext = gameManager.getUserContextByUsername(userWantFriend.getUsername());
        if (userWantFriendContext != null) {
            userWantFriendContext.getChannel().writeAndFlush(new AreYouAddFriendPacket(
                    userContext.getUsername(),
                    userContext.getUserId()).build().getPacket());
        }
    }

    public void requestRejectApproveFriend(UserContext userContext, RejectApproveFriendReq req) {
        
        UserEntity userWantRejectApprove = userRepo.findById(req.getUserId()).orElse(null);
        if (userWantRejectApprove == null) return;

        UserFriendRequestEntity usersFriendRequest = userFriendRequestRepo.findByUsernameAndUsernameRequest(userContext.getUsername(), userWantRejectApprove.getUsername());
        if (usersFriendRequest == null) return;
        if (req.isApprove()) {
            UserFriendEntity usersFriendEntity = new UserFriendEntity();
            usersFriendEntity.setUsername(userContext.getUsername());
            usersFriendEntity.setFriendUsername(userWantRejectApprove.getUsername());
            usersFriendEntity.setStatus(1);
            usersFriendEntity.setCreateAt(new Date());
            userFriendRepo.save(usersFriendEntity);

            usersFriendEntity = new UserFriendEntity();
            usersFriendEntity.setUsername(userWantRejectApprove.getUsername());
            usersFriendEntity.setFriendUsername(userContext.getUsername());
            usersFriendEntity.setStatus(1);
            usersFriendEntity.setCreateAt(new Date());
            userFriendRepo.save(usersFriendEntity);
        }
        usersFriendRequest.setStatus(0);
        usersFriendRequest.setUpdateAt(new Date());
        userFriendRequestRepo.save(usersFriendRequest);
        getUserFriendList(userContext, UserConstant.TYPE_FRIEND_LIST_PENDING);
    }

    public void deleteFriendUser(UserContext userContext, long userId) {
        

        UserEntity userWantDeleteFriend = userRepo.findById(userId).orElse(null);
        if (userWantDeleteFriend == null) return;

        UserFriendEntity usersFriendEntity = userFriendRepo.findByUsernameAndFriendUsername(userContext.getUsername(), userWantDeleteFriend.getUsername());
        if (usersFriendEntity != null) {
            usersFriendEntity.setStatus(0);
            usersFriendEntity.setUpdateAt(new Date());
            userFriendRepo.save(usersFriendEntity);
        }
        usersFriendEntity = userFriendRepo.findByUsernameAndFriendUsername(userWantDeleteFriend.getUsername(), userContext.getUsername());
        if (usersFriendEntity != null) {
            usersFriendEntity.setStatus(0);
            usersFriendEntity.setUpdateAt(new Date());
            userFriendRepo.save(usersFriendEntity);
        }
        getUserFriendList(userContext, UserConstant.TYPE_FRIEND_LIST_ACCEPTED);
    }

    public void blockFriendUser(UserContext userContext, long userId) {
        
        UserEntity userWantBlock = userRepo.findById(userId).orElse(null);
        if (userWantBlock == null) return;
        if (userBlockFriendRepo.findByUsernameAndUsernameBlock(userContext.getUsername(), userWantBlock.getUsername()) != null)
            return;
        if (userFriendRequestRepo.findByUsernameAndUsernameRequest(userContext.getUsername(), userWantBlock.getUsername()) != null)
            return;

        userFriendRepo.updateByUsernameAndFriendUsername(userContext.getUsername(), userWantBlock.getUsername(), 0);
        userFriendRepo.updateByUsernameAndFriendUsername(userWantBlock.getUsername(), userContext.getUsername(), 0);

        UserBlockFriendEntity userBlockFriendEntity = new UserBlockFriendEntity();
        userBlockFriendEntity.setUsername(userContext.getUsername());
        userBlockFriendEntity.setUsernameBlock(userWantBlock.getUsername());
        userBlockFriendEntity.setStatus(1);
        userBlockFriendEntity.setCreateAt(new Date());
        userBlockFriendRepo.save(userBlockFriendEntity);

        userContext.getChannel().writeAndFlush(new SendBlockUserFriendPacket(userContext.getUserId(), 0).build().getPacket());
        getUserFriendList(userContext, UserConstant.TYPE_FRIEND_LIST_ACCEPTED);
    }

    public void unblockFriendUser(UserContext userContext, long userId) {
        
        UserEntity userWantBlock = userRepo.findById(userId).orElse(null);
        if (userWantBlock == null) return;
        UserBlockFriendEntity userBlockFriendEntity = userBlockFriendRepo.findByUsernameAndUsernameBlock(userContext.getUsername(), userWantBlock.getUsername());
        if (userBlockFriendEntity == null) return;
        userBlockFriendEntity.setStatus(0);
        userBlockFriendEntity.setUpdateAt(new Date());
        userBlockFriendRepo.save(userBlockFriendEntity);
        getUserFriendList(userContext, UserConstant.TYPE_FRIEND_LIST_BLOCKED);
    }

    public List<UserEntity> getUserFriendByType(UserContext userContext, int type) {
        List<UserEntity> userFriends = new ArrayList<>();
        if (type == UserConstant.TYPE_FRIEND_LIST_ACCEPTED) {
            userFriends = userFriendRepo.findAllUserFriendByUsername(userContext.getUsername());
        } else if (type == UserConstant.TYPE_FRIEND_LIST_BLOCKED) {
            userFriends = userBlockFriendRepo.findAllUserFriendByUsername(userContext.getUsername());
        } else if (type == UserConstant.TYPE_FRIEND_LIST_PENDING) {
            userFriends = userFriendRequestRepo.findAllUserFriendByUsername(userContext.getUsername());
        }
        return userFriends;
    }
}
