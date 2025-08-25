package home.thienph.xyahoo_server.data.mapping.packet;

import home.thienph.xyahoo_server.data.mapping.APacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class CacheImagePacket extends APacketPipeline {
    int sourceId;
    String pathImage;

    public CacheImagePacket(int sourceId, String pathImage) {
        super(120);
        this.sourceId = sourceId;
        this.pathImage = pathImage;
    }

    @Override
    @SneakyThrows
    public CacheImagePacket build() {
        packet.getPayload().writeInt(sourceId);
        XByteBuf.writeByteArray(packet.getPayload(), Files.readAllBytes(Path.of(pathImage)));
        return this;
    }
}
