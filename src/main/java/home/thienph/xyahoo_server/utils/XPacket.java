package home.thienph.xyahoo_server.utils;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.MarqueeMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.CreateSimpleDialogProcess;
import home.thienph.xyahoo_server.data.users.UserContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class XPacket {
    public void showSimpleDialog(UserContext userContext, String message) {
        userContext.getChannel().writeAndFlush(GameProcessPacketPipeline.newInstance()
                .addPipeline(new CreateSimpleDialogProcess(message))
                .endPipeline()
                .build().getPacket());
    }

    public void showSimpleMarquee(UserContext userContext, String message) {
        userContext.getChannel().writeAndFlush(new MarqueeMessagePacket(message).build().getPacket());
    }
}
