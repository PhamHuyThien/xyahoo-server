package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import lombok.SneakyThrows;

public class CacheImagePacket extends APacketPipeline {
    int sourceId;
    byte[] data;

    public CacheImagePacket(int sourceId, byte[] data) {
        super(120);
        this.sourceId = sourceId;
        this.data = data;
    }

    @Override
    @SneakyThrows
    public CacheImagePacket build() {
        packet.getPayload().writeInt(sourceId);
        XByteBuf.writeByteArray(packet.getPayload(), data);
        return this;
    }
}
