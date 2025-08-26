package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;

public class MultiSelectListComponentProcess implements IGameProcessPacketPipeline {
    int screenId;
    int componentId;
    boolean enabled;

    public MultiSelectListComponentProcess(int screenId, int componentId, boolean enabled) {
        this.screenId = screenId;
        this.componentId = componentId;
        this.enabled = enabled;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(30);
        payload.writeInt(screenId);
        payload.writeInt(componentId);
        payload.writeBoolean(enabled);
    }
}
