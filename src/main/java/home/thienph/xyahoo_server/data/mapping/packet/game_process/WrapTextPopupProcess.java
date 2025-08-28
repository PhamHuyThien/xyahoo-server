package home.thienph.xyahoo_server.data.mapping.packet.game_process;

import home.thienph.xyahoo_server.utils.XByteBuf;
import io.netty.buffer.ByteBuf;

public class WrapTextPopupProcess implements IGameProcessPacketPipeline {
    String text;

    public WrapTextPopupProcess(String text) {
        this.text = text;
    }

    @Override
    public void build(ByteBuf payload) {
        payload.writeInt(28);
        XByteBuf.writeString(payload, text);
    }
}
