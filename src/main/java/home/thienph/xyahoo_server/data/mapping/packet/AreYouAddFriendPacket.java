package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class AreYouAddFriendPacket extends APacketPipeline {

    String username;
    long userId;

    public AreYouAddFriendPacket(String username, long userId) {
        super(1009);
        this.username = username;
        this.userId = userId;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), username);
        packet.getPayload().writeLong(userId);
        return this;
    }
}
