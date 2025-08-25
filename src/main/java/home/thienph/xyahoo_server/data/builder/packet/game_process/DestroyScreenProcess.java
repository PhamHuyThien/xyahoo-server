package home.thienph.xyahoo_server.data.builder.packet.game_process;

import io.netty.buffer.ByteBuf;

public class DestroyScreenProcess implements IGameProcessPacketPipeline {
    int screenId;

    public DestroyScreenProcess(int screenId) {
        this.screenId = screenId;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(0);
        payload.writeInt(screenId);
    }
}
