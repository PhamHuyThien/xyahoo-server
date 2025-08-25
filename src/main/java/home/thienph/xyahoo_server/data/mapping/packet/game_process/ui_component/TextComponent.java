package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class TextComponent extends AComponent {
    String label;
    int width;
    int textColor;
    byte alignment;

    public TextComponent(String label, int width, int textColor, byte alignment) {
        super(1);
        this.label = label;
        this.width = width;
        this.textColor = textColor;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeInt(width);
        payload.writeInt(textColor);
        payload.writeByte(alignment);
    }
}
