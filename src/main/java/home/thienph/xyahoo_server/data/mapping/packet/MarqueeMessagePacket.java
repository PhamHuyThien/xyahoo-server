package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class MarqueeMessagePacket extends APacketPipeline {

    String message;

    public MarqueeMessagePacket(String message) {
        super(3417);
        this.message = message;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), message);
        return this;
    }
}
