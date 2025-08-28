package home.thienph.xyahoo_server.controllers;

import home.thienph.xyahoo_server.anotations.HasRole;
import home.thienph.xyahoo_server.anotations.PacketMapping;
import home.thienph.xyahoo_server.constants.*;
import home.thienph.xyahoo_server.data.base.Packet;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.ReceiveChatRoomMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.UserBuzzPacket;
import home.thienph.xyahoo_server.data.mapping.packet.UserChatPacket;
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
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ChatController {

    @Autowired
    GameManager gameManager;

    @Autowired
    HomeCommandService homeCommandService;

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.SEND_MESSAGE)
    public void chat(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomKey = XByteBuf.readString(packet.getPayload());
        String message = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getChannels().forEach(userJoinchannel -> {
            ReceiveChatRoomMessagePacket receiveChatRoomMessagePacket = new ReceiveChatRoomMessagePacket(userContext.getUsername(), message, 1);
            userJoinchannel.writeAndFlush(receiveChatRoomMessagePacket.build().getPacket());
        });
    }


    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.LEAVE_CHAT_ROOM)
    public void leaveChatRoom(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomKey = XByteBuf.readString(packet.getPayload());

        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null) return;

        roomContext.getChannels().remove(channel);
        roomContext.getUsers().remove(userContext);
        roomContext.update();

        homeCommandService.homeSelectRoom(channel, packet.getPayload());
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_CHAT)
    public void userChat(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        long userId = packet.getPayload().readLong();
        String message = XByteBuf.readString(packet.getPayload());
        Channel userReceiver = gameManager.getChannelByUserId(userId);
        if (userReceiver != null)
            userReceiver.writeAndFlush(new UserChatPacket(userContext.getId(), message).build().getPacket());
    }


    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.USER_BUZZ)
    public void userBuzz(Channel channel, Packet packet) {
        long userId = packet.getPayload().readLong();
        UserContext userContext = gameManager.getUserContext(channel);
        Channel userReceiver = gameManager.getChannelByUserId(userId);
        if (userReceiver != null)
            userReceiver.writeAndFlush(new UserBuzzPacket(userContext.getId()).build().getPacket());
    }

    @SneakyThrows
    @HasRole({UserConstant.ROLE_USER})
    @PacketMapping(commandId = ClientCommandConst.VIEW_USER_IN_ROOM)
    public void viewUserInRoom(Channel channel, Packet packet) {
        UserContext userContext = gameManager.getUserContext(channel);
        String roomKey = XByteBuf.readString(packet.getPayload());
        RoomContext roomContext = gameManager.getRoomContextByRoomKey(roomKey);
        if (roomContext == null || !roomContext.getChannels().contains(channel)) return;

        GameProcessPacketPipeline viewUserInRoomPipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(NewDialogProcess.createDefault(roomContext.getRoom().getRoomName(), ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID))
                .addPipeline(() -> {

                    GameProcessPacketPipeline clickUserInRoomAction = GameProcessPacketPipeline.newInstance().addPipeline(() -> {
                        GameProcessPacketPipeline actionRefreshRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    var componentsAction = List.of(GetDataComponent.createGetSourceTypeServerDefault(ScreenConstant.ROOM_LIST_FRIEND_SCREEN_ID, ComponentConstant.ROOM_LIST_FRIEND_IN_ROOM_COMPONENT_ID));
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
                        contextMenus.add(new ContextMenu("Làm mới", actionRefreshRoom));
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
}
