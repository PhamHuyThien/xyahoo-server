package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class ReceiveChatRoomMessagePacket extends APacketPipeline {
    String senderName;
    String messageContent;
    int messageType;

    public ReceiveChatRoomMessagePacket(String senderName, String messageContent, int messageType) {
        super(4804);
        this.senderName = senderName;
        this.messageContent = messageContent;
        this.messageType = messageType;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(0);
        XByteBuf.writeString(packet.getPayload(), senderName);
        XByteBuf.writeString(packet.getPayload(), messageContent);
        packet.getPayload().writeInt(0);
        packet.getPayload().writeInt(messageType);
        return this;
    }
}
