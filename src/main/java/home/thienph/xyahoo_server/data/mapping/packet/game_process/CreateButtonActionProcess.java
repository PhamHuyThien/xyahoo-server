package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class CreateButtonActionProcess implements IGameProcessPacketPipeline {
    int screenId;
    byte softKeyId; //0 left,1 center, 2 right
    String buttonName;
    GameProcessPacketPipeline action;

    public CreateButtonActionProcess(int screenId, byte softKeyId, String buttonName, GameProcessPacketPipeline action) {
        this.screenId = screenId;
        this.softKeyId = softKeyId;
        this.buttonName = buttonName;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(5);
        payload.writeInt(screenId);
        payload.writeByte(softKeyId);
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
        XByteBuf.writeString(payload, buttonName);
    }
}
