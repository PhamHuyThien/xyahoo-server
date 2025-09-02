package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class UpdateUserIsOnlinePacket extends APacketPipeline {
    long userId;
    int isOnline;

    public UpdateUserIsOnlinePacket(long userId, int isOnline) {
        super(5000031);
        this.userId = userId;
        this.isOnline = isOnline;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(userId);
        packet.getPayload().writeByte(isOnline);
        return this;
    }
}
