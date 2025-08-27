package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class UserBuzzPacket extends APacketPipeline {
    Long userId;

    public UserBuzzPacket(Long userId) {
        super(5000016);
        this.userId = userId;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(userId);
        return this;
    }
}
