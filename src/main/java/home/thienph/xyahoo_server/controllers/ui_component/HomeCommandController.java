package home.thienph.xyahoo_server.controllers.ui_component;

import home.thienph.xyahoo_server.anotations.CommandMapping;
import home.thienph.xyahoo_server.anotations.UIComponentController;
import home.thienph.xyahoo_server.constants.CommandGetUIConstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@UIComponentController
public class HomeCommandController {

    @CommandMapping(commandId = CommandGetUIConstant.HOME_SELECT_INDEX)
    public void homeSelectIndex(Channel channel, ByteBuf payload) {
        int index = payload.readInt();
        
    }
}
