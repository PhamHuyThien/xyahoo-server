package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;

public interface IGameProcessPacketPipeline {

    void build(ByteBuf payload);

}
