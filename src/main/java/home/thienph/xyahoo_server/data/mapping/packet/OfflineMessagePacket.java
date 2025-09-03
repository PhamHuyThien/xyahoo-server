package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.data.resources.MessageOffline;
import home.thienph.xyahoo_server.utils.XByteBuf;

import java.util.List;

public class OfflineMessagePacket extends APacketPipeline {

    List<MessageOffline> messagesOffline;

    public OfflineMessagePacket(List<MessageOffline> messagesOffline) {
        super(5000032);
        this.messagesOffline = messagesOffline;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeInt(messagesOffline.size());
        for (MessageOffline messageOffline : messagesOffline) {
            XByteBuf.writeString(packet.getPayload(), messageOffline.getUsername());
            XByteBuf.writeString(packet.getPayload(), messageOffline.getMessage());
            XByteBuf.writeString(packet.getPayload(), messageOffline.getTimestamp());
            packet.getPayload().writeLong(messageOffline.getUserId());
        }
        return this;
    }
}
