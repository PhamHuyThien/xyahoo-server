package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class HandlerFriendAcceptPacket extends APacketPipeline {
    boolean isAccept;
    long userId;
    String username;
    int statusCode;
    int data;

    public HandlerFriendAcceptPacket(boolean isAccept, long userId, String username, int statusCode, int data) {
        super(5000026);
        this.isAccept = isAccept;
        this.userId = userId;
        this.username = username;
        this.statusCode = statusCode;
        this.data = data;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeBoolean(isAccept);
        packet.getPayload().writeLong(userId);
        XByteBuf.writeString(packet.getPayload(), username);
        packet.getPayload().writeInt(statusCode);
        packet.getPayload().writeInt(data);
        return this;
    }
}
