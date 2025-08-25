package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class FriendListPacket extends APacketPipeline {
    List<BuddyInfo> friends;

    public FriendListPacket(List<BuddyInfo> friends) {
        super(5000028);
        this.friends = friends;
    }

    @Override
    public APacketPipeline build() {
        ByteBuf payload = packet.getPayload();
        payload.writeInt(friends.size());
        for (BuddyInfo friend : friends) {
            payload.writeLong(friend.getContactId());
            XByteBuf.writeString(payload, friend.getUsername());
            XByteBuf.writeString(payload, friend.getDescription());
        }
        return this;
    }
}
