package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class CheckBoxComponent extends AComponent {
    String label;
    boolean checked;
    byte alignment;

    public CheckBoxComponent(String label, boolean checked, byte alignment) {
        super(14);
        this.label = label;
        this.checked = checked;
        this.alignment = alignment;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, label);
        payload.writeBoolean(checked);
        payload.writeByte(alignment);
    }
}
