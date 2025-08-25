package home.thienph.xyahoo_server.data.mapping.packet.game_process.ui_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class LabelComponent extends AComponent {
    String label;
    int textColor;
    byte alignment;

    public LabelComponent(String label, int textColor, byte alignment) {
        super(15);
        this.label = label;
        this.textColor = textColor;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeInt(textColor);
        payload.writeByte(alignment);
    }
}
