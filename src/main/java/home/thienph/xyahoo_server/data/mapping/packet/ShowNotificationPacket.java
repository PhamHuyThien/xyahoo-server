package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class ShowNotificationPacket extends APacketPipeline {
    String content;

    public ShowNotificationPacket(String content) {
        super(4809);
        this.content = content;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), content);
        return this;
    }
}
