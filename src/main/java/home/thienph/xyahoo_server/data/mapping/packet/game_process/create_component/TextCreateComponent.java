package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class TextCreateComponent extends ACreateComponent {
    String label;
    int width;
    int textColor;
    byte alignment;

    public TextCreateComponent(String label, int width, int textColor, byte alignment) {
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
