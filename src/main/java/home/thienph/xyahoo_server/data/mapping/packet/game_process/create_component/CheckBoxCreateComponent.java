package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class CheckBoxCreateComponent extends ACreateComponent {
    String label;
    boolean checked;
    byte alignment;

    public CheckBoxCreateComponent(String label, boolean checked, byte alignment) {
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
