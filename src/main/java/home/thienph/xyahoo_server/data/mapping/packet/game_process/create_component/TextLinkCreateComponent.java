package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.data.mapping.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class TextLinkCreateComponent extends ACreateComponent {
    String text;
    int width;
    int textColor;
    GameProcessPacketPipeline action;
    byte alignment;

    public TextLinkCreateComponent(String text, int width, int textColor, GameProcessPacketPipeline action) {
        super(4);
        this.text = text;
        this.width = width;
        this.textColor = textColor;
        this.action = action;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, text);
        payload.writeInt(width);
        payload.writeInt(textColor);
        XByteBuf.writeByteArray(payload, action.endPipeline().getPayloadPipeline().array());
        payload.writeByte(alignment);
    }
}
