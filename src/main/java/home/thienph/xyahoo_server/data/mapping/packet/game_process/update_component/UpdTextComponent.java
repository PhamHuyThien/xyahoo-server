package home.thienph.xyahoo_server.data.mapping.packet.game_process.update_component;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class UpdTextComponent extends AUpdateComponent {
    String displayText;

    public UpdTextComponent(int componentId, String displayText) {
        super(componentId, 0);
        this.displayText = displayText;
    }

    @Override
    public void build(ByteBuf payload) {
        XByteBuf.writeString(payload, displayText);
    }
}
