package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;

public class SwitchScreenProcess implements IGameProcessPacketPipeline {
    int screenId;

    public SwitchScreenProcess(int screenId) {
        this.screenId = screenId;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(50);
        payload.writeInt(screenId);
    }
}
