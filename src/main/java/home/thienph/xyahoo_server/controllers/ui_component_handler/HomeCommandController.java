package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.constants.ComponentConstant;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.*;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component.ListComponent;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component.PopupDialogComponent;
import home.thienph.xyahoo_server.data.resources.ContextMenu;
import home.thienph.xyahoo_server.data.resources.GetDataComponent;
import home.thienph.xyahoo_server.managers.GameManager;
import home.thienph.xyahoo_server.utils.XImage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@UIComponentController
public class HomeCommandController {

    @Autowired
    GameManager gameManager;

    @SneakyThrows
    @CommandMapping(commandId = ScreenConstant.ROOM_SCREEN_ID)
    public void homeSelectIndex(Channel channel, ByteBuf payload) {
        GameProcessPacketPipeline gameProcessPacketPipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(new NewDialogProcess(ScreenConstant.ROOM_SCREEN_TITLE, ScreenConstant.ROOM_SCREEN_ID, true))
                .addPipeline(() -> {
                    GameProcessPacketPipeline selectRoomAction = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                List<GetDataComponent> componentsAction = new ArrayList<>();
                                componentsAction.add(new GetDataComponent((byte) 1, 1, ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, false, null));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_SELECT_INDEX, componentsAction);
                            });

                    return new UIComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, new ListComponent(true, (byte) 0, (byte) 3, 10, 10, (byte) 1, gameManager.getRoomContexts(), selectRoomAction));
                })
//                .addPipeline(new MultiSelectListComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, true))
                .addPipeline(() -> {
                    GameProcessPacketPipeline action = GameProcessPacketPipeline.newInstance().addPipeline(() -> {
                        GameProcessPacketPipeline actionCreateRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    GameProcessPacketPipeline actionJoinRoom = GameProcessPacketPipeline.newInstance()
                                            .addPipeline(() -> {
                                                List<GetDataComponent> componentsAction = new ArrayList<>();
                                                componentsAction.add(new GetDataComponent((byte) 1, 1, ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID, true, null));
                                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_CREATE_NEW_ROOM, componentsAction);
                                            });
                                    PopupDialogComponent popupDialogComponent = new PopupDialogComponent("Vui lòng nhập Tên phòng:", (byte) 0, actionJoinRoom);
                                    return new UIComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID, popupDialogComponent);
                                })
                                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID));
                        List<ContextMenu> contextMenus = new ArrayList<>();
                        contextMenus.add(new ContextMenu("Tạo phòng", actionCreateRoom));
                        contextMenus.add(new ContextMenu("Tham gia", GameProcessPacketPipeline.newInstance()));
                        return new CreateContextMenuProcess((byte) 0, contextMenus);
                    });

                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, (byte) 0, "Menu", action);
                })
                .addPipeline(() -> {
                    GameProcessPacketPipeline action = GameProcessPacketPipeline.newInstance().addPipeline(new SwitchScreenProcess(ScreenConstant.MAIN_SCREEN_ID));
                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, (byte) 2, "Đóng", action);
                })
                .addPipeline(new LoadingProcess(false))
                .endPipeline().build();
        channel.writeAndFlush(gameProcessPacketPipeline.getPacket());
    }
}
