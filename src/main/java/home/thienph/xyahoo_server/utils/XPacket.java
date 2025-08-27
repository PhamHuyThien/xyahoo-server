package home.thienph.xyahoo_server.utils;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.data.mapping.packet.MarqueeMessagePacket;
import home.thienph.xyahoo_server.data.mapping.packet.game_process.CreateSimpleDialogProcess;
import io.netty.channel.Channel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class XPacket {
    public void showSimpleDialog(Channel channel, String message) {
        channel.writeAndFlush(GameProcessPacketPipeline.newInstance()
                .addPipeline(new CreateSimpleDialogProcess(message))
                .endPipeline()
                .build().getPacket());
    }

    public void showSimpleMarquee(Channel channel, String message) {
        channel.writeAndFlush(new MarqueeMessagePacket(message).build().getPacket());
    }
}
