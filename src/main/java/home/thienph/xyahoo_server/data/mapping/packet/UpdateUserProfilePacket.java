package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;

import java.util.List;

public class UpdateUserProfilePacket extends APacketPipeline {
    Long accountId;
    List<Long> friendIds;
    Integer saveContactStatus;

    public UpdateUserProfilePacket(Long accountId, List<Long> friendIds, Integer saveContactStatus) {
        super(5000015);
        this.accountId = accountId;
        this.friendIds = friendIds;
        this.saveContactStatus = saveContactStatus;
    }

    @Override
    public APacketPipeline build() {
        packet.getPayload().writeLong(accountId);
        packet.getPayload().writeInt(friendIds == null ? 0 : friendIds.size());
        if (friendIds != null) {
            for (Long friendId : friendIds) {
                packet.getPayload().writeLong(friendId);
            }
        }
        if (saveContactStatus != null)
            packet.getPayload().writeInt(saveContactStatus);
        return this;
    }
}
