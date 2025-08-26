package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import org.apache.logging.log4j.util.Strings;

public class JoinChatRoomPacket extends APacketPipeline {
    boolean success;
    String roomName;
    String roomId;
    long ownerAccountId;

    public JoinChatRoomPacket(boolean success, String roomName, String roomId, long ownerAccountId) {
        super(4801);
        this.success = success;
        this.roomName = roomName;
        this.roomId = roomId;
        this.ownerAccountId = ownerAccountId;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeBoolean(success);
        XByteBuf.writeString(packet.getPayload(), roomName);
        XByteBuf.writeString(packet.getPayload(), roomId);
        packet.getPayload().writeLong(ownerAccountId);
        packet.getPayload().writeLong(0);
        XByteBuf.writeString(packet.getPayload(), Strings.EMPTY);
        return this;
    }
}
