package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class ShowInviteChatDialogPacket extends APacketPipeline {
    String userInvite;
    String roomName;
    String string1;
    String string2;

    public ShowInviteChatDialogPacket(String userInvite, String roomName, String string1, String string2) {
        super(4807);
        this.userInvite = userInvite;
        this.roomName = roomName;
        this.string1 = string1;
        this.string2 = string2;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), userInvite);
        XByteBuf.writeString(packet.getPayload(), roomName);
        XByteBuf.writeString(packet.getPayload(), string1);
        XByteBuf.writeString(packet.getPayload(), string2);
        return this;
    }
}
