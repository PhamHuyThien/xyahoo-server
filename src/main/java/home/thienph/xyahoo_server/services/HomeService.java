package home.thienph.xyahoo_server.services;

import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import home.thienph.xyahoo_server.constants.ComponentConstant;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.CreateComponentProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.GetDataUIComponentProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.NewDialogProcess;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component.GridCreateComponent;
import home.thienph.xyahoo_server.data.resources.GetDataComponent;
import home.thienph.xyahoo_server.data.resources.GridComp;
import home.thienph.xyahoo_server.managers.GameManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {
    @Autowired
    GameManager gameManager;

    public void showHome(Channel channel) {
        GameProcessPacketPipeline pipeline = GameProcessPacketPipeline.newInstance()
                .addPipeline(() -> NewDialogProcess.createDefault(ScreenConstant.MAIN_SCREEN_TITLE, ScreenConstant.MAIN_SCREEN_ID))
                .addPipeline(() -> {
                    var homeAction = GameProcessPacketPipeline.newInstance()
                            .addPipeline(() -> {
                                var componentsAction = List.of(GetDataComponent.createGetDataIntegerDefault(ScreenConstant.MAIN_SCREEN_ID, ScreenConstant.FRIEND_SCREEN_ID));
                                return new GetDataUIComponentProcess(CommandGetUIConstant.HOME_SELECT_INDEX, componentsAction);
                            });
                    var gridHomeData = gameManager.getGameHomes().stream().map(GridComp::new).toList();
                    var homeGridComp = GridCreateComponent.createDefault(gridHomeData, homeAction);
                    return new CreateComponentProcess(ScreenConstant.MAIN_SCREEN_ID, ComponentConstant.MAIN_GRID_COMPONENT_ID, homeGridComp);
                })
                .endPipeline();
        channel.writeAndFlush(pipeline.build().getPacket());
    }
}
