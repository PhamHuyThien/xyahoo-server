package home.thienph.xyahoo_server.data.builder.packet.game_process;

import io.netty.buffer.ByteBuf;

public class CloseCurrentDialogProcess implements IGameProcessPacketPipeline {
    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(23);
    }
}
