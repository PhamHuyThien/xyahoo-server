package home.thienph.xyahoo_server.services.ui_component_handler;

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
import home.thienph.xyahoo_server.data.users.UserContext;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeCommandService {

    @Autowired
    GameManager gameManager;


    public void homeSelectRoom(UserContext userContext, ByteBuf payload) {
        GameProcessPacketPipeline gameProcessPacketPipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(NewDialogProcess.createDefault(ScreenConstant.ROOM_SCREEN_TITLE, ScreenConstant.ROOM_SCREEN_ID))
                .addPipeline(() -> {
                    var selectRoomAction = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_SELECT_INDEX, componentsAction);
                            });
                    var homeGrid = gameManager.getRoomContexts().stream().map(ListComp::new).toList();
                    return new CreateComponentProcess(
                            ScreenConstant.ROOM_SCREEN_ID,
                            ComponentConstant.ROOM_LIST_COMPONENT_ID,
                            ListCreateComponent.createListDefault(homeGrid, selectRoomAction));
                })
//                .addPipeline(new MultiSelectListComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, true))
                .addPipeline(() -> {
                    GameProcessPacketPipeline action = GameProcessPacketPipeline.newInstance().addPipeline(() -> {
                        GameProcessPacketPipeline actionCreateRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    GameProcessPacketPipeline actionConfirmCreateRoom = GameProcessPacketPipeline.newInstance()
                                            .addPipeline(() -> {
                                                var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID));
                                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_CREATE_NEW_ROOM, componentsAction);
                                            });
                                    var popupDialogComponent = new PopupDialogCreateComponent("Vui lòng nhập Tên phòng:", PopupDialogCreateComponent.DIALOG_TYPE_OK, actionConfirmCreateRoom);
                                    return new CreateComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID, popupDialogComponent);
                                })
                                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID));
                        GameProcessPacketPipeline actionRefreshRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    var componentsAction = List.of(GetDataComponent.createGetSourceTypeServerDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_REFRESH_ROOM, componentsAction);
                                });
                        GameProcessPacketPipeline actionJoinRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    var componentsAction = List.of(GetDataComponent.createGetDataStringDefault(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_SELECT_INDEX, componentsAction);
                                });
                        List<ContextMenu> contextMenus = new ArrayList<>();
                        contextMenus.add(new ContextMenu("Tạo phòng", actionCreateRoom));
                        contextMenus.add(new ContextMenu("Tham gia", actionJoinRoom));
                        contextMenus.add(new ContextMenu("Làm mới", actionRefreshRoom));
                        return new CreateContextMenuProcess(CreateContextMenuProcess.MENU_TYPE_LEFT, contextMenus);
                    });
                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, CreateButtonActionProcess.BUTTON_TYPE_LEFT, "Menu", action);
                })
                .addPipeline(() -> {
                    var action = GameProcessPacketPipeline.newInstance().addPipeline(new DestroyScreenProcess(ScreenConstant.ROOM_SCREEN_ID));
                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, CreateContextMenuProcess.MENU_TYPE_RIGHT, "Đóng", action);
                })
                .addPipeline(new LoadingProcess(false))
                .endPipeline().build();
        userContext.getChannel().writeAndFlush(gameProcessPacketPipeline.getPacket());
    }
}
