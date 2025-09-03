package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class PushRemoveFriendPacket extends APacketPipeline {
    long userId;
    int dataToSave;

    public PushRemoveFriendPacket(long userId, int dataToSave) {
        super(5000024);
        this.userId = userId;
        this.dataToSave = dataToSave;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(userId);
        packet.getPayload().writeInt(dataToSave);
        return this;
    }
}
