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
import home.thienph.xyahoo_server.services.ui_component_handler.RoomCommandService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ChatService {
    @Autowired
    GameManager gameManager;
    @Autowired
    RoomCommandService roomCommandService;

    public void showFriendInRoom(Channel channel, String roomKey) {
        UserContext userContext = gameManager.getUserContext(channel);
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null || !roomContext.getChannels().contains(channel)) return;

        GameProcessPacketPipeline viewUserInRoomPipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(NewDialogProcess.createDefault(roomContext.getRoom().getRoomName(), ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID))
                .addPipeline(() -> {

                    GameProcessPacketPipeline clickUserInRoomAction = GameProcessPacketPipeline.newInstance().addPipeline(() -> {
                        GameProcessPacketPipeline actionRefreshFriendListInRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_REFRESH_LIST, componentsAction);
                                });
                        GameProcessPacketPipeline addFriendAction = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_FRIEND_LIST_IN_ROOM_ADD_FRIEND, componentsAction);
                                });
                        List<ContextMenu> contextMenus = new ArrayList<>();
                        contextMenus.add(new ContextMenu("Hồ sơ", new GameProcessPacketPipeline()));
                        contextMenus.add(new ContextMenu("Kết bạn", addFriendAction));
                        contextMenus.add(new ContextMenu("Làm mới", actionRefreshFriendListInRoom));
                        return new CreateContextMenuProcess(CreateContextMenuProcess.MENU_TYPE_CENTER, contextMenus);
                    });

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
        channel.writeAndFlush(viewUserInRoomPipeline.getPacket());
    }

    public void roomClickInviteUser(Channel channel, String roomKey) {
        if (!userIsOwnerRoom(gameManager.getUserContext(channel), roomKey)) return;
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);

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
                .endPipeline().build().flushPipeline(channel);
    }

    public void acceptInviteJoinRoom(Channel channel, String roomKey, String password) {
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;
        if (roomContext.getRoom().getPassword() != null
                && !roomContext.getRoom().getPassword().isEmpty()
                && !roomContext.getRoom().getPassword().equals(password)) return;
        roomCommandService.joinChatRoom(channel, roomKey);
    }

    public boolean userIsOwnerRoom(UserContext userContext, String roomKey) {
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return false;
        return Objects.equals(roomContext.getRoom().getUserOwnerId(), userContext.getId());
    }
}
