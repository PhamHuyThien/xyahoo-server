package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

public class SendBlockUserFriendPacket extends APacketPipeline {
    long userId;
    int saveData;

    public SendBlockUserFriendPacket(long userId, int saveData) {
        super(5000019);
        this.userId = userId;
        this.saveData = saveData;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(userId);
        packet.getPayload().writeInt(saveData);
        return this;
    }
}
