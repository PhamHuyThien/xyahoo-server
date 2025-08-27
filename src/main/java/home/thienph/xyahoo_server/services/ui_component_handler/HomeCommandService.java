package home.thienph.xyahoo_server.services.ui_component_handler;

import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.constants.ComponentConstant;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.*;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component.ListComponent;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component.PopupDialogComponent;
import home.thienph.xyahoo_server.data.resources.ContextMenu;
import home.thienph.xyahoo_server.data.resources.GetDataComponent;
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


    public void homeSelectRoom(Channel channel, ByteBuf payload) {
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
                                    GameProcessPacketPipeline actionConfirmCreateRoom = GameProcessPacketPipeline.newInstance()
                                            .addPipeline(() -> {
                                                List<GetDataComponent> componentsAction = new ArrayList<>();
                                                componentsAction.add(new GetDataComponent((byte) 1, 1, ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID, true, null));
                                                return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_CREATE_NEW_ROOM, componentsAction);
                                            });
                                    PopupDialogComponent popupDialogComponent = new PopupDialogComponent("Vui lòng nhập Tên phòng:", (byte) 0, actionConfirmCreateRoom);
                                    return new UIComponentProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID, popupDialogComponent);
                                })
                                .addPipeline(() -> new ShowTextInputDialogProcess(ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_CREATE_ROOM_COMPONENT_ID));
                        GameProcessPacketPipeline actionRefreshRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    List<GetDataComponent> componentsAction = new ArrayList<>();
                                    componentsAction.add(new GetDataComponent((byte) 0, 0, ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, false, 0));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_REFRESH_ROOM, componentsAction);
                                });
                        GameProcessPacketPipeline actionJoinRoom = GameProcessPacketPipeline.newInstance()
                                .addPipeline(() -> {
                                    List<GetDataComponent> componentsAction = new ArrayList<>();
                                    componentsAction.add(new GetDataComponent((byte) 1, 1, ScreenConstant.ROOM_SCREEN_ID, ComponentConstant.ROOM_LIST_COMPONENT_ID, false, null));
                                    return new GetDataUIComponentProcess(CommandGetUIConstant.ROOM_SELECT_INDEX, componentsAction);
                                });
                        List<ContextMenu> contextMenus = new ArrayList<>();
                        contextMenus.add(new ContextMenu("Tạo phòng", actionCreateRoom));
                        contextMenus.add(new ContextMenu("Tham gia", actionJoinRoom));
                        contextMenus.add(new ContextMenu("Làm mới", actionRefreshRoom));
                        return new CreateContextMenuProcess((byte) 0, contextMenus);
                    });

                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, (byte) 0, "Menu", action);
                })
                .addPipeline(() -> {
                    GameProcessPacketPipeline action = GameProcessPacketPipeline.newInstance()
                            .addPipeline(new DestroyScreenProcess(ScreenConstant.ROOM_SCREEN_ID));
                    return new CreateButtonActionProcess(ScreenConstant.ROOM_SCREEN_ID, (byte) 2, "Đóng", action);
                })
                .addPipeline(new LoadingProcess(false))
                .endPipeline().build();
        channel.writeAndFlush(gameProcessPacketPipeline.getPacket());
    }
}
