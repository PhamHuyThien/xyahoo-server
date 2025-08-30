package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.constants.ComponentConstant;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.*;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component.ListCreateComponent;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component.PopupDialogCreateComponent;
import home.thienph.xyahoo_server.data.resources.ContextMenu;
import home.thienph.xyahoo_server.data.resources.GetDataComponent;
import home.thienph.xyahoo_server.data.resources.ListComp;
import home.thienph.xyahoo_server.data.users.ResourceContext;
import home.thienph.xyahoo_server.data.users.RoomContext;
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.repositories.RoomRepo;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.services.ui_component_handler.RoomCommandService;
import home.thienph.xyahoo_server.utils.XDate;
import home.thienph.xyahoo_server.utils.XPacket;
import home.thienph.xyahoo_server.utils.XThread;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {
    @Autowired
    GameManager gameManager;

    @Lazy
    @Autowired
    RoomCommandService roomCommandService;

    @Autowired
    RoomRepo roomRepo;

    @Autowired
    HomeCommandService homeCommandService;

    public void showFriendInRoom(UserContext userContext, String roomKey) {
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null || !roomContext.getUsers().contains(userContext)) {
            XPacket.showSimpleDialog(userContext, "Bạn chưa tham gia phòng nây");
            return;
        }
        GameProcessPacketPipeline viewUserInRoomPipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(NewDialogProcess.createDefault(roomContext.getRoom().getRoomName(), ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID))
                .addPipeline(() -> {
                    var clickUserInRoomAction = createMenuContextShowFriendInRoom(userContext, roomKey);
                    ResourceContext icon = gameManager.getResourceContextById(104);
                    List<ListComp> listUserInRoomData = roomContext.getUsers().stream().map(uc -> new ListComp(uc, icon)).toList();
                    return new CreateComponentProcess(
                            ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID,
                            ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID,
                            ListCreateComponent.createListDefault(listUserInRoomData, clickUserInRoomAction));
                })
                .addPipeline(() -> {
                    var action = GameProcessPacketPipeline.newInstance().addPipeline(new DestroyScreenProcess(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID));
                    return new CreateButtonActionProcess(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, CreateContextMenuProcess.MENU_TYPE_RIGHT, "Đóng", action);
                })
                .endPipeline().build();
        userContext.getChannel().writeAndFlush(viewUserInRoomPipeline.getPacket());
    }

    public void roomClickInviteUser(UserContext userContext, String roomKey) {
        if (!userIsOwnerRoom(userContext, roomKey)) return;
        GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    GameProcessPacketPipeline actionConfirmCreateRoom = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_ADD_USER_IN_ROOM_COMPONENT_ID));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_ADD_USER_IN_ROOM, componentsAction);
                            });
                    var popupDialogComponent = new PopupDialogCreateComponent("Vui lòng nhập tên người mời:", PopupDialogCreateComponent.DIALOG_TYPE_OK, actionConfirmCreateRoom);
                    return new CreateComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_ADD_USER_IN_ROOM_COMPONENT_ID, popupDialogComponent);
                })
                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_ADD_USER_IN_ROOM_COMPONENT_ID))
                .addPipeline(new FocusComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_ADD_USER_IN_ROOM_COMPONENT_ID))
                .endPipeline().build().flushPipeline(userContext);
    }

    public void acceptInviteJoinRoom(UserContext userContext, String roomKey, String password) {
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;
        if (roomContext.getRoom().getPassword() != null
                && !roomContext.getRoom().getPassword().isEmpty()
                && !roomContext.getRoom().getPassword().equals(password)) return;
        roomCommandService.joinChatRoom(userContext, roomKey, password);
    }

    public boolean userIsOwnerRoom(UserContext userContext, String roomKey) {
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return false;
        return Objects.equals(roomContext.getRoom().getUserOwnerId(), userContext.getUserId());
    }

    public RoomContext getCurrentOwnerRoom(UserContext userContext) {
        return gameManager.getRoomContexts().stream().filter(roomContext ->
                        roomContext.getUsers().stream().anyMatch(user -> user.getUsername().equals(userContext.getUsername())))
                .findFirst().orElse(null);
    }

    private GameProcessPacketPipeline createMenuContextShowFriendInRoom(UserContext userContext, String roomKey) {
        boolean isOwnerRoom = userIsOwnerRoom(userContext, roomKey);

        var actionRefreshFriendListInRoom = GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_REFRESH_LIST, componentsAction);
                });
        var addFriendAction = GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_ADD_FRIEND, componentsAction);
                });
        var kickUserAction = GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_KICK_USER, componentsAction);
                });
        return GameProcessPacketPipeline.newInstance().addPipeline(() -> {
            List<ContextMenu> contextMenus = new ArrayList<>();
            contextMenus.add(new ContextMenu("Hồ sơ", new GameProcessPacketPipeline()));
            contextMenus.add(new ContextMenu("Kết bạn", addFriendAction));
            if (isOwnerRoom) contextMenus.add(new ContextMenu("Đá khỏi phòng", kickUserAction));
            contextMenus.add(new ContextMenu("Làm mới", actionRefreshFriendListInRoom));
            return new CreateContextMenuProcess(CreateContextMenuProcess.MENU_TYPE_CENTER, contextMenus);
        });
    }

    public void roomClickRenameRoom(UserContext userContext, String roomKey) {
        if (!userIsOwnerRoom(userContext, roomKey)) return;
        GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var actionConfirmRenameRoom = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_RENAME_COMPONENT_ID));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_RENAME_ROOM, componentsAction);
                            });
                    var popupDialogComponent = new PopupDialogCreateComponent("Vui lòng nhập tên phòng mới:", PopupDialogCreateComponent.DIALOG_TYPE_OK, actionConfirmRenameRoom);
                    return new CreateComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_RENAME_COMPONENT_ID, popupDialogComponent);
                })
                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_RENAME_COMPONENT_ID))
                .addPipeline(new FocusComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_RENAME_COMPONENT_ID))
                .endPipeline().build().flushPipeline(userContext);
    }

    public void roomClickChangePasswordRoom(UserContext userContext, String roomKey) {
        if (!userIsOwnerRoom(userContext, roomKey)) return;
        GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var actionConfirmChangePasswordRoom = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_CHANGE_PASSWORD_COMPONENT_ID));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_CHANGE_PASSWORD_ROOM, componentsAction);
                            });
                    var popupDialogComponent = new PopupDialogCreateComponent("Vui lòng nhập mật khẩu mới:", PopupDialogCreateComponent.DIALOG_TYPE_OK, actionConfirmChangePasswordRoom);
                    return new CreateComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_CHANGE_PASSWORD_COMPONENT_ID, popupDialogComponent);
                })
                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_CHANGE_PASSWORD_COMPONENT_ID))
                .addPipeline(new FocusComponentProcess(ScreenConstant.DEFAULT_SCREEN_ID, ComponentConstant.ROOM_CHANGE_PASSWORD_COMPONENT_ID))
                .endPipeline().build().flushPipeline(userContext);
    }

    public void showPopupInputPasswordRoom(UserContext userContext) {
        GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> {
                    var actionConfirmChangePasswordRoom = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(
                                        GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_INPUT_PASSWORD_COMPONENT_ID),
                                        GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID)
                                );
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_INPUT_PASSWORD_ROOM, componentsAction);
                            });
                    var popupDialogComponent = new PopupDialogCreateComponent("Vui lòng nhập mật khẩu phòng:", PopupDialogCreateComponent.DIALOG_TYPE_OK, actionConfirmChangePasswordRoom);
                    return new CreateComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_INPUT_PASSWORD_COMPONENT_ID, popupDialogComponent);
                })
                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_INPUT_PASSWORD_COMPONENT_ID))
                .addPipeline(new FocusComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_INPUT_PASSWORD_COMPONENT_ID))
                .endPipeline().build().flushPipeline(userContext);
    }

    public void roomClickExtendRoom(UserContext userContext, String roomKey) {
        if (!userIsOwnerRoom(userContext, roomKey)) return;
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        Date extendDate = new Date(roomContext.getRoom().getExpireAt().getTime() + 1000 * 60 * 60 * 24);
        roomContext.getRoom().setExpireAt(extendDate);
        roomContext.getRoom().setUpdateAt(new Date());
        roomRepo.save(roomContext.getRoom());
        roomContext.update();
        XPacket.showSimpleDialog(userContext, "Được gia hạn tới " + XDate.format(extendDate));
    }

    @SneakyThrows
    public void roomClickDeleteRoom(UserContext userContext, String roomKey) {
        if (!userIsOwnerRoom(userContext, roomKey)) return;
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if(roomContext == null) return;
        roomContext.getRoom().setIsDelete(1);
        roomContext.getRoom().setUpdateAt(new Date());
        roomRepo.save(roomContext.getRoom());
        roomContext.update();
        gameManager.loadAllRoomContexts();

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        roomContext.getUsers().forEach(user -> threadPoolExecutor.submit(() -> {
            GameProcessPacketPipeline.newInstance()
                    .addPipeline(new SwitchScreenProcess(ScreenConstant.ROOM_SCREEN_ID))
                    .addPipeline(new DestroyScreenByTitleProcess("P. " + roomContext.getRoom().getRoomName())).endPipeline()
                    .endPipeline().build().flushPipeline(user);
            XThread.sleep(1000);
            homeCommandService.homeSelectRoom(user, null);
            XPacket.showSimpleMarquee(user, "Phòng " + roomContext.getRoom().getRoomName() + " đã bị xóa");
        }));
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS);
        XPacket.showSimpleDialog(userContext, "Đã xóa phòng");
    }
}
