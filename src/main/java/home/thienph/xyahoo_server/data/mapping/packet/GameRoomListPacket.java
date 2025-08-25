package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.friends.BuddyInfo;
import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

import java.util.List;

public class GameRoomListPacket extends APacketPipeline {
    List<BuddyInfo> buddyInfoList;

    public GameRoomListPacket(List<BuddyInfo> buddyInfoList) {
        super(5000009);
        this.buddyInfoList = buddyInfoList;
    }

    @Override
    public APacketPipeline build() {
        ByteBuf payload = packet.getPayload();
        payload.writeInt(buddyInfoList.size());
        for(BuddyInfo buddyInfo : buddyInfoList) {
            XByteBuf.writeString(payload, buddyInfo.getGroupName());
            XByteBuf.writeString(payload, buddyInfo.getUsername());
            XByteBuf.writeString(payload, buddyInfo.getDisplayName());
            payload.writeInt(100);
            payload.writeInt(200);
        }
        return this;
    }
}
