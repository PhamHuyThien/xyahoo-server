package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.constants.ComponentConstant;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.GetDataUIComponentProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.NewDialogProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.UIComponentProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component.GridComponent;
import home.thienph.xyahoo_server.data.resources.GetDataComponent;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    @Autowired
    GameManager gameManager;

    public void createScreenHome(Channel channel) {
    }
    
    public void showHome(Channel channel) {
        GameProcessPacketPipeline pipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> new NewDialogProcess(ScreenConstant.MAIN_SCREEN_TITLE, ScreenConstant.MAIN_SCREEN_ID, true))
                .addPipeline(() -> {
                    GameProcessPacketPipeline homeAction = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                List<GetDataComponent> componentsAction = new ArrayList<>();
                                componentsAction.add(new GetDataComponent((byte) 0, 1, ScreenConstant.MAIN_SCREEN_ID, ScreenConstant.FRIEND_SCREEN_ID, false, null));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.HOME_SELECT_INDEX, componentsAction);
                            });
                    GridComponent homeGrid = new GridComponent(40, 40, true, gameManager.getHomeGrids(), homeAction);
                    return new UIComponentProcess(ScreenConstant.MAIN_SCREEN_ID, ComponentConstant.MAIN_GRID_COMPONENT_ID, homeGrid);
                })
                .endPipeline();
        channel.writeAndFlush(pipeline.build().getPacket());
    }
}
