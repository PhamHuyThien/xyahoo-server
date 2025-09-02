package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class UpdateStatusTextUserPacket extends APacketPipeline {
    long userId;
    String statusText;

    public UpdateStatusTextUserPacket(long userId, String statusText) {
        super(5000035);
        this.userId = userId;
        this.statusText = statusText;
    }

    @Override
    public APacketPipeline build() {
        ByteBuf payload = packet.getPayload();
        payload.writeLong(userId);
        XByteBuf.writeString(payload, statusText);
        return this;
    }
}
