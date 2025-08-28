package home.thienph.xyahoo_server.data.mapping.packet.game_process.create_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class SimpleTextCreateComponent extends ACreateComponent {
    String text;
    int type;

    public SimpleTextCreateComponent(String text, int type) {
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
