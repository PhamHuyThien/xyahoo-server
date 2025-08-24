package home.thienph.xyahoo_server.data.builder.packet.game_process.ui_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class SimpleTextComponent extends AComponent {
    String text;
    int type;

    public SimpleTextComponent(String text, int type) {
        super(5);
        this.text = text;
        this.type = type;
    }


    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, text);
        payload.writeInt(type);
    }
}
