package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.data.builder.packet.GameProcessPacketPipeline;
import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class TextLinkComponent extends AComponent {
    String text;
    int width;
    int textColor;
    GameProcessPacketPipeline action;
    byte alignment;

    public TextLinkComponent(String text, int width, int textColor, GameProcessPacketPipeline action) {
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
