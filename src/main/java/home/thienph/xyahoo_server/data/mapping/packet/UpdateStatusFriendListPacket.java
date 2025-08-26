package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.data.repo.UserFriendDto;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class UpdateStatusFriendListPacket extends APacketPipeline {
    List<UserFriendDto> friends;

    public UpdateStatusFriendListPacket(List<UserFriendDto> friends) {
        super(5000029);
        this.friends = friends;
    }

    @Override
    public APacketPipeline build() {
        ByteBuf payload = packet.getPayload();
        payload.writeInt(friends.size());
        for (UserFriendDto friend : friends) {
            payload.writeLong(friend.getUserId());
            XByteBuf.writeString(payload, friend.getStatusText());
        }
        return this;
    }
}
