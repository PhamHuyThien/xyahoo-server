package home.thienph.xyahoo_server.services.ui_component_handler;

import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.PacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.JoinChatRoomPacket;
import home.thienph.xyahoo_server.data.mapping.packet.MarqueeMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.ShowInviteChatDialogPacket;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.DestroyScreenByTitleProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.SwitchScreenProcess;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.entities.RoomEntity;
import home.thienph.xyahoo_server.entities.RoomGroupEntity;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.RoomGroupRepo;
import home.thienph.xyahoo_server.repositories.RoomRepo;
import home.thienph.xyahoo_server.services.ChatService;
import home.thienph.xyahoo_server.utils.XPacket;
import home.thienph.xyahoo_server.utils.XString;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RoomCommandService {
    @Autowired
    GameManager gameManager;
    @Autowired
    RoomGroupRepo roomGroupRepo;
    @Autowired
    RoomRepo roomRepo;
    @Autowired
    HomeCommandService homeCommandService;
    @Autowired
    ChatService chatService;

    public void roomCreateNewRoom(Channel channel, String roomName) {
        UserContext userContext = gameManager.getUserContext(channel);
        RoomGroupEntity roomGroupEntity = roomGroupRepo.findFirstByIsForUser(1).orElse(null);
        if (roomGroupEntity == null) {
            XPacket.showSimpleDialog(channel, "Không thể tạo phòng chat mới");
            return;
        }
        if (roomRepo.existsByRoomName(roomName)) {
            XPacket.showSimpleDialog(channel, "Tên phòng này đã có người đặt rồi");
            return;
        }

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(roomName);
        roomEntity.setUserOwnerId(userContext.getId());
        roomEntity.setMaxUser(20);
        roomEntity.setRoomKey(XString.replaceSpaceWithUnderscore(XString.removeVietnameseDiacritics(roomName)));
        roomEntity.setIconId(104);
        roomEntity.setCreateAt(new Date());
        roomEntity.setUserOwnerId(userContext.getId());
        roomEntity.setRoomId(roomGroupEntity.getId());
        roomRepo.save(roomEntity);

        gameManager.loadAllRoomContexts();
        homeCommandService.homeSelectRoom(channel, null);
    }

    public void joinChatRoom(Channel channel, String roomKey) {
        if (roomKey == null || roomKey.isEmpty()) return;

        UserContext userContext = gameManager.getUserContext(channel);
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        if (!roomContext.getChannels().contains(channel)) {
            roomContext.getChannels().add(channel);
            roomContext.getUsers().add(userContext);
            roomContext.update();
        }

        long roomOwnerId = roomContext.getRoom().getUserOwnerId() == null ? -999L : roomContext.getRoom().getUserOwnerId();
        JoinChatRoomPacket joinChatRoomPacket = new JoinChatRoomPacket(true, roomContext.getRoom().getRoomName(), roomKey, roomOwnerId);
        channel.writeAndFlush(joinChatRoomPacket.build().getPacket());
    }

    public void roomAddUserInRoom(Channel channel, String usernameInvite) {
        UserContext userContext = gameManager.getUserContext(channel);
        if (usernameInvite.isEmpty()) return;
        Channel userChannelInvite = gameManager.getChannelByUsername(usernameInvite);
        if (userChannelInvite == null) {
            XPacket.showSimpleDialog(channel, "Người chơi không hoạt động");
            return;
        }
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(channel, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(channel, "Bạn không phải chủ phòng");
            return;
        }
        new ShowInviteChatDialogPacket(
                userContext.getUsername(),
                currentOwnerRoom.getRoom().getRoomName(),
                currentOwnerRoom.getRoom().getRoomKey(),
                currentOwnerRoom.getRoom().getPassword()
        ).build().flush(userChannelInvite);
    }

    public void roomKickUserInRoom(Channel channel, String usernameKick) {
        UserContext userContext = gameManager.getUserContext(channel);
        if (usernameKick.isEmpty()) return;
        if (userContext.getUsername().equals(usernameKick)) {
            XPacket.showSimpleDialog(channel, "Không thể kick chinh mình");
            return;
        }
        Channel userChannelKick = gameManager.getChannelByUsername(usernameKick);
        UserContext userContextKick = gameManager.getUserContext(userChannelKick);
        if (userChannelKick == null) {
            XPacket.showSimpleDialog(channel, "Người chơi không hoạt động");
            return;
        }
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(channel, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(channel, "Bạn không phải chủ phòng");
            return;
        }
        currentOwnerRoom.getChannels().remove(userChannelKick);
        currentOwnerRoom.getUsers().remove(userContextKick);
        PacketPipeline.newInstance()
                .addPipeline(GameProcessPacketPipeline.newInstance()
                        .addPipeline(new SwitchScreenProcess(ScreenConstant.ROOM_SCREEN_ID))
                        .addPipeline(new DestroyScreenByTitleProcess("P. " + currentOwnerRoom.getRoom().getRoomName())).endPipeline())
                .addPipeline(new MarqueeMessagePacket("Bạn bị đá ra khỏi phòng P. " + currentOwnerRoom.getRoom().getRoomName()))
                .endPipeline().flushPipeline(userChannelKick);
    }

    public void refreshListUserInRoom(Channel channel, String usernameInRoom) {
        if (usernameInRoom == null || usernameInRoom.isEmpty()) return;
        gameManager.getRoomContexts().stream().filter(roomContext ->
                        roomContext.getUsers().stream().anyMatch(user -> user.getUsername().equals(usernameInRoom))).findFirst()
                .ifPresent(roomContext -> chatService.showFriendInRoom(channel, roomContext.getRoom().getRoomKey()));
    }

}
