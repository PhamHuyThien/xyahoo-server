package home.thienph.xyahoo_server.controllers.ui_component_handler;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.ScreenConstant;
import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.MarqueeMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.WrapTextPopupProcess;
import home.thienph.xyahoo_server.services.ui_component_handler.HomeCommandService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@UIComponentController
public class HomeCommandController {


    @Autowired
    HomeCommandService homeCommandService;

    @SneakyThrows
    @CommandMapping(commandId = ScreenConstant.ROOM_SCREEN_ID)
    public void homeSelectRoom(Channel channel, ByteBuf payload) {
        homeCommandService.homeSelectRoom(channel, payload);
    }

    @SneakyThrows
    @CommandMapping(commandId = ScreenConstant.TOP_SCREEN_ID)
    public void homeSelectTop(Channel channel, ByteBuf payload) {
//        channel.writeAndFlush(new MarqueeMessagePacket("vcl").build().getPacket());
        channel.writeAndFlush(GameProcessPacketPipeline.newInstance()
                .addPipeline(new WrapTextPopupProcess("fjdfjds sdfhsushd usdfhdsjfhdsjh jshdjfhsjfhsdfjhsjhfjdhsjdfhsjfshd sdhfjsdfh sjfsjh sfdjsd fhsdjfhdj hf"))
                .endPipeline().build().getPacket());
    }
}
