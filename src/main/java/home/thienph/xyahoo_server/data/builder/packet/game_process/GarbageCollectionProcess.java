package home.thienph.xyahoo_server.data.builder.packet.game_process;

import io.netty.buffer.ByteBuf;

public class GarbageCollectionProcess implements IGameProcessPacketPipeline {
    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(8);
    }
}
