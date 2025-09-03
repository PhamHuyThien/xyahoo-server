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

    public void roomCreateNewRoom(UserContext userContext, String roomName) {

        RoomGroupEntity roomGroupEntity = roomGroupRepo.findFirstByIsForUser(1).orElse(null);
        if (roomGroupEntity == null) {
            XPacket.showSimpleDialog(userContext, "Không thể tạo phòng chat mới");
            return;
        }
//        if (roomRepo.existsByRoomName(roomName)) {
//            XPacket.showSimpleDialog(userContext, "Tên phòng này đã có người đặt rồi");
//            return;
//        }

        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(roomName);
        roomEntity.setUserOwnerId(userContext.getUserId());
        roomEntity.setMaxUser(20);
        roomEntity.setRoomKey(XString.replaceSpaceWithUnderscore(XString.removeVietnameseDiacritics(roomName)));
        roomEntity.setIconId(104);
        roomEntity.setCreateAt(new Date());
        roomEntity.setExpireAt(new Date(new Date().getTime() + 1000 * 60 * 60 * 24));
        roomEntity.setUserOwnerId(userContext.getUserId());
        roomEntity.setRoomGroupId(roomGroupEntity.getId());
        roomEntity.setIsDelete(0);
        roomRepo.save(roomEntity);

        gameManager.loadAllRoomContexts();
        homeCommandService.homeSelectRoom(userContext, null);
        XPacket.showSimpleMarquee(userContext, "Đã tạo phòng " + roomName);
    }

    public void joinChatRoom(UserContext userContext, String roomKey, String roomPassword) {
        if (roomKey == null || roomKey.isEmpty()) return;

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        if (roomContext.getUsers().size() >= roomContext.getRoom().getMaxUser()) {
            XPacket.showSimpleDialog(userContext, "Phòng đã đạt số lượng người tối đa");
            return;
        }

        if (roomContext.getRoom().getPassword() != null
                && !roomContext.getRoom().getPassword().isEmpty()
                && !roomContext.getRoom().getUserOwnerId().equals(userContext.getUserId())
                && (roomPassword == null || !roomPassword.equals(roomContext.getRoom().getPassword()))) {
            chatService.showPopupInputPasswordRoom(userContext);
            return;
        }


        if (!roomContext.getUsers().contains(userContext)) {
            roomContext.getUsers().add(userContext);
            roomContext.update();
        }

        long roomOwnerId = roomContext.getRoom().getUserOwnerId() == null ? -999L : roomContext.getRoom().getUserOwnerId();
        JoinChatRoomPacket joinChatRoomPacket = new JoinChatRoomPacket(true, roomContext.getRoom().getRoomName(), roomKey, roomOwnerId);
        userContext.getChannel().writeAndFlush(joinChatRoomPacket.build().getPacket());
    }

    public void roomAddUserInRoom(UserContext userContext, String usernameInvite) {

        if (usernameInvite.isEmpty()) return;
        UserContext userChannelInvite = gameManager.getUserContextByUsername(usernameInvite);
        if (userChannelInvite == null) {
            XPacket.showSimpleDialog(userContext, "Người chơi không hoạt động");
            return;
        }
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(userContext, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(userContext, "Bạn không phải chủ phòng");
            return;
        }
        new ShowInviteChatDialogPacket(
                userContext.getUsername(),
                currentOwnerRoom.getRoom().getRoomName(),
                currentOwnerRoom.getRoom().getRoomKey(),
                currentOwnerRoom.getRoom().getPassword()
        ).build().flush(userChannelInvite);
    }

    public void roomKickUserInRoom(UserContext userContext, String usernameKick) {

        if (usernameKick.isEmpty()) return;
        if (userContext.getUsername().equals(usernameKick)) {
            XPacket.showSimpleDialog(userContext, "Không thể kick chinh mình");
            return;
        }
        UserContext userContextKick = gameManager.getUserContextByUsername(usernameKick);
        if (userContextKick == null) {
            XPacket.showSimpleDialog(userContext, "Người chơi không hoạt động");
            return;
        }
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(userContext, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(userContext, "Bạn không phải chủ phòng");
            return;
        }
        currentOwnerRoom.getUsers().remove(userContextKick);
        PacketPipeline.newInstance()
                .addPipeline(GameProcessPacketPipeline.newInstance()
                        .addPipeline(new SwitchScreenProcess(ScreenConstant.ROOM_SCREEN_ID))
                        .addPipeline(new DestroyScreenByTitleProcess("P. " + currentOwnerRoom.getRoom().getRoomName())).endPipeline())
                .addPipeline(new MarqueeMessagePacket("Bạn bị đá ra khỏi phòng P. " + currentOwnerRoom.getRoom().getRoomName()))
                .endPipeline().flushPipeline(userContextKick);
    }

    public void refreshListUserInRoom(UserContext userContext, String usernameInRoom) {
        if (usernameInRoom == null || usernameInRoom.isEmpty()) return;
        gameManager.getRoomContexts().stream().filter(roomContext ->
                        roomContext.getUsers().stream().anyMatch(user -> user.getUsername().equals(usernameInRoom))).findFirst()
                .ifPresent(roomContext -> chatService.showFriendInRoom(userContext, roomContext.getRoom().getRoomKey()));
    }

    public void roomRenameRoom(UserContext userContext, String newRoomName) {
        if (newRoomName == null || newRoomName.isEmpty()) return;
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(userContext, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(userContext, "Bạn không phải chủ phòng");
            return;
        }

        String oldRoomName = currentOwnerRoom.getRoom().getRoomName();

        currentOwnerRoom.getRoom().setRoomName(newRoomName);
        currentOwnerRoom.getRoom().setUpdateAt(new Date());
        roomRepo.save(currentOwnerRoom.getRoom());
        currentOwnerRoom.update();


        currentOwnerRoom.getUsers().forEach(user -> {
            GameProcessPacketPipeline.newInstance()
                    .addPipeline(new SwitchScreenProcess(ScreenConstant.ROOM_SCREEN_ID))
                    .addPipeline(new DestroyScreenByTitleProcess("P. " + oldRoomName)).endPipeline()
                    .endPipeline().build().flushPipeline(user)
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            Thread.sleep(50);
                            chatService.acceptInviteJoinRoom(user,
                                    currentOwnerRoom.getRoom().getRoomKey(),
                                    currentOwnerRoom.getRoom().getPassword());
                        }
                    });
        });
        XPacket.showSimpleDialog(userContext, "Đổi tên phòng thành công");
    }

    public void roomChangePassword(UserContext userContext, String newPasswordRoom) {
//        if (newPasswordRoom == null || newPasswordRoom.isEmpty()) return;
        RoomContext currentOwnerRoom = chatService.getCurrentOwnerRoom(userContext);
        if (currentOwnerRoom == null) {
            XPacket.showSimpleDialog(userContext, "Không tìm thấy phòng");
            return;
        }
        if (!chatService.userIsOwnerRoom(userContext, currentOwnerRoom.getRoom().getRoomKey())) {
            XPacket.showSimpleDialog(userContext, "Bạn không phải chủ phòng");
            return;
        }
        currentOwnerRoom.getRoom().setPassword(newPasswordRoom);
        currentOwnerRoom.getRoom().setUpdateAt(new Date());
        roomRepo.save(currentOwnerRoom.getRoom());
        currentOwnerRoom.update();

        if (newPasswordRoom == null || newPasswordRoom.isEmpty()) {
            XPacket.showSimpleDialog(userContext, "Đã xoá mật khẩu phòng");
            return;
        }
        XPacket.showSimpleDialog(userContext, "Đổi mật khẩu phòng thành công");
    }


    public void handlerRoomInputPassword(UserContext userContext, String roomKey, String passwordRoom) {
        if (passwordRoom == null || passwordRoom.isEmpty()) return;
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) {
            XPacket.showSimpleDialog(userContext, "Không tìm thấy phòng");
            return;
        }
        if (!passwordRoom.equals(roomContext.getRoom().getPassword())) {
            XPacket.showSimpleDialog(userContext, "Mật khất không chính xác");
            return;
        }
        chatService.acceptInviteJoinRoom(userContext, roomKey, passwordRoom);
    }
}
