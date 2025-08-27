package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class UserChatPacket extends APacketPipeline {
    Long userId;
    String message;

    public UserChatPacket(Long userId, String message) {
        super(5000023);
        this.userId = userId;
        this.message = message;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(userId);
        XByteBuf.writeString(packet.getPayload(), message);
        return this;
    }
}
