package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class CreateButtonActionProcess implements IGameProcessPacketPipeline {
    public static final int BUTTON_TYPE_LEFT = 0;
    public static final int BUTTON_TYPE_CENTER = 1;
    public static final int BUTTON_TYPE_RIGHT = 2;
    int screenId;
    int buttonType; //0 left,1 center, 2 right
    String buttonName;
    GameProcessPacketPipeline action;

    public CreateButtonActionProcess(int screenId, int buttonType, String buttonName, GameProcessPacketPipeline action) {
        this.screenId = screenId;
        this.buttonType = buttonType;
        this.buttonName = buttonName;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(5);
        payload.writeInt(screenId);
        payload.writeByte(buttonType);
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
        XByteBuf.writeString(payload, buttonName);
    }
}
