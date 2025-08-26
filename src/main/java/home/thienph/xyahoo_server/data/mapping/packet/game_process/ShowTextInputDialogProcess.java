package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import io.netty.buffer.ByteBuf;

public class ShowTextInputDialogProcess implements IGameProcessPacketPipeline {
    int screenId;
    int componentId;

    public ShowTextInputDialogProcess(int screenId, int componentId) {
        this.screenId = screenId;
        this.componentId = componentId;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(56);
        payload.writeInt(screenId);
        payload.writeInt(componentId);
    }
}
