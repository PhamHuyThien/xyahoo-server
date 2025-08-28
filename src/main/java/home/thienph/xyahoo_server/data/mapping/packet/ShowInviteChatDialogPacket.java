package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;

public class ShowInviteChatDialogPacket extends APacketPipeline {
    String userInvite;
    String roomName;
    String roomKey;
    String password;

    public ShowInviteChatDialogPacket(String userInvite, String roomName, String roomKey, String password) {
        super(4807);
        this.userInvite = userInvite;
        this.roomName = roomName;
        this.roomKey = roomKey;
        this.password = password;
    }

    @Override
    public APacketPipeline build() {
        XByteBuf.writeString(packet.getPayload(), userInvite);
        XByteBuf.writeString(packet.getPayload(), roomName);
        XByteBuf.writeString(packet.getPayload(), roomKey);
        XByteBuf.writeString(packet.getPayload(), password);
        return this;
    }
}
