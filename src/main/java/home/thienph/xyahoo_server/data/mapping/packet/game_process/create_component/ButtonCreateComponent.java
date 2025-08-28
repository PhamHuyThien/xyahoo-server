package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class ButtonCreateComponent extends ACreateComponent {
    String label;
    int width;
    GameProcessPacketPipeline action;

    public ButtonCreateComponent(String label, int width, GameProcessPacketPipeline action) {
        super(17);
        this.label = label;
        this.width = width;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeInt(width);
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
    }
}
